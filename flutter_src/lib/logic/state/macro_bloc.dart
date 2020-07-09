import 'package:flutter_form_bloc/flutter_form_bloc.dart';
import 'package:macrobaseapp/logic/usecases/macro_firestore/firestore_macro_operation.dart';
import 'package:macrobaseapp/model/adapters/action_model.dart';
import 'package:macrobaseapp/model/adapters/macro_model.dart';
import 'package:macrobaseapp/model/adapters/trigger_model.dart';
import 'package:macrobaseapp/model/entities/action.dart' as Model;
import 'package:macrobaseapp/model/entities/macro.dart';
import 'package:macrobaseapp/model/entities/trigger.dart';
import 'package:macrobaseapp/model/entities/user.dart';

class WizardFormBloc extends FormBloc<String, String> {
  final User user;

  final macroName = TextFieldBloc(
    validators: [
      FieldBlocValidators.required,
    ],
    asyncValidators: [asyncValidator],
    name: 'Macro Name',
  );
  static Future<String> asyncValidator(String name) async {
    //Avoid too many Firestore calls
    await Future.delayed(Duration(milliseconds: 200));
    List<Macro> list = await queryMacro(name);
    if (list.length > 0)
      return "Sorry, this macro name already exist";
    else
      return null;
  }

  final description = TextFieldBloc(
    validators: [
      FieldBlocValidators.required,
    ],
  );

  final actionType = SelectFieldBloc(
    name: 'Action Type',
    validators: [
      FieldBlocValidators.required,
    ],
    items: [Model.Action.SHEET_ACTION, Model.Action.POLL_ACTION],
  );

  final sheetAppendAction = TextFieldBloc(
    name: "Sheet URL",
    validators: [
      FieldBlocValidators.required,
    ],
  );

  final triggerType = SelectFieldBloc(
    name: 'Trigger Type',
    validators: [
      FieldBlocValidators.required,
    ],
    items: [Trigger.COMMAND_BASED, Trigger.TIME_BASED],
  );

  final commandTrigger = TextFieldBloc(
    name: 'command',
    validators: [
      FieldBlocValidators.required,
    ],
  );

  WizardFormBloc({this.user}) {
    addFieldBlocs(
      step: 0,
      fieldBlocs: [macroName, description],
    );
    addFieldBlocs(
      step: 1,
      fieldBlocs: [sheetAppendAction],
    );
    addFieldBlocs(
      step: 2,
      fieldBlocs: [commandTrigger],
    );
  }

  @override
  void onSubmitting() async {
    if (state.currentStep == 0) {
      await Future.delayed(Duration(milliseconds: 500));
      emitSuccess();
    } else if (state.currentStep == 1) {
      emitSuccess();
    } else if (state.currentStep == 2) {
      await Future.delayed(Duration(milliseconds: 500));

      dynamic trigger;
      dynamic action;

      switch (actionType.value) {
        default:
          {
            action = new SheetAppendActionModel(
              sheetUrl: sheetAppendAction.value,
              columnValue: ["1", "2", "3"],
            );
          }
          break;
      }

      switch (triggerType.value) {
        default:
          {
            trigger = new CommandTriggerModel(command: commandTrigger.value);
          }
          break;
      }

      final macro = MacroModel(
        macroName: macroName.value.trim(),
        description: description.value.trim(),
        creatorId: this.user.email,
        trigger: trigger,
        action: action,
      );

      uploadMacro(macro.toJson());

      emitSuccess();
    }
  }
}

//    //Expand hidden fields of trigger
//    triggerTypeBloc.field.onValueChanges(onData: (_, current) async* {
//      removeFieldBlocs(
//        fieldBlocs: [commandTriggerFieldBloc.field, timeTriggerFieldBloc.field],
//      );
//      if (current.value == Trigger.COMMAND_BASED) {
//        addFieldBlocs(fieldBlocs: [commandTriggerFieldBloc.field]);
//      } else if (current.value == Trigger.TIME_BASED) {
//        addFieldBlocs(fieldBlocs: [timeTriggerFieldBloc.field]);
//      }
//    });
//
//    actionTypeBloc.field.onValueChanges(onData: (_, current) async* {
//      removeFieldBlocs(
//        fieldBlocs: [pollActionFieldBloc.field, sheetUrlBloc.field],
//      );
//      if (current.value == Action.POLL_ACTION) {
//        addFieldBlocs(fieldBlocs: [pollActionFieldBloc.field]);
//      } else if (current.value == Action.SHEET_ACTION) {
//        addFieldBlocs(fieldBlocs: [sheetUrlBloc.field]);
//      }
//    });
//  }

