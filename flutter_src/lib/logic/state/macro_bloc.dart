import 'dart:convert';

import 'package:flutter_form_bloc/flutter_form_bloc.dart';
import 'package:macrobaseapp/logic/state/macro_bloc_validator.dart';
import 'package:macrobaseapp/logic/usecases/macro_firestore/firestore_macro_operation.dart';
import 'package:macrobaseapp/model/adapters/action_model.dart';
import 'package:macrobaseapp/model/adapters/macro_model.dart';
import 'package:macrobaseapp/model/adapters/trigger_model.dart';
import 'package:macrobaseapp/model/entities/action.dart';
import 'package:macrobaseapp/model/entities/trigger.dart';
import 'package:macrobaseapp/model/entities/user.dart';

class WizardFormBloc extends FormBloc<String, String> {
  final User user;

  TextFieldBloc macroName = TextFieldBloc(
    validators: [
      FieldBlocValidators.required,
    ],
    asyncValidators: [CustomBlocValidator.nameValidator],
    name: 'Macro Name',
  );

  TextFieldBloc description = TextFieldBloc(
    validators: [
      FieldBlocValidators.required,
    ],
  );

  SelectFieldBloc actionType = SelectFieldBloc(
    name: 'Action Type',
    validators: [
      FieldBlocValidators.required,
    ],
    items: [Action.SHEET_ACTION, Action.POLL_ACTION],
  );

  SelectFieldBloc sheetActionType = SelectFieldBloc(
    name: 'Sheet Action Type',
    validators: [
      FieldBlocValidators.required,
    ],
    items: [SheetAction.APPEND_ACTION, SheetAction.READ_ACTION],
  );

  TextFieldBloc actionSheetUrl = TextFieldBloc(
    name: "Sheet URL",
    validators: [
      FieldBlocValidators.required,
      CustomBlocValidator.sheetUrlValidator,
    ],
  );

  ListFieldBloc<TextFieldBloc> actionSheetColumn = ListFieldBloc<TextFieldBloc>();

  SelectFieldBloc triggerType = SelectFieldBloc(
    name: 'Trigger Type',
    validators: [
      FieldBlocValidators.required,
    ],
    items: [Trigger.COMMAND_BASED, Trigger.TIME_BASED],
  );

  TextFieldBloc triggerCommand = TextFieldBloc(
    name: 'command',
    validators: [
      FieldBlocValidators.required,
    ],
  );

  WizardFormBloc({this.user}) {
    // Default Setup
    addFieldBlocs(
      step: 0,
      fieldBlocs: [macroName, description],
    );
    addFieldBlocs(
      step: 1,
      fieldBlocs: [actionType, sheetActionType, actionSheetUrl, actionSheetColumn],
    );
    addFieldBlocs(
      step: 2,
      fieldBlocs: [triggerType, triggerCommand],
    );

    actionTypeSetup();
    sheetActionSetup();
    triggerTypeSetup();
  }

  void triggerTypeSetup() {
    triggerType.onValueChanges(onData: (_, current) async* {
      removeFieldBlocs(fieldBlocs: [triggerCommand]);
      if (current.value == Trigger.COMMAND_BASED) {
        addFieldBlocs(step: 2, fieldBlocs: [triggerCommand]);
      }
    });
  }

  void actionTypeSetup() {
    actionType.onValueChanges(onData: (_, current) async* {
      removeFieldBlocs(fieldBlocs: [sheetActionType, actionSheetUrl, actionSheetColumn] + actionSheetColumn.value.toList());
      if (current.value == Action.SHEET_ACTION) {
        addFieldBlocs(step: 1, fieldBlocs: [sheetActionType, actionSheetUrl]);
      }
    });
  }

  void sheetActionSetup() {
    sheetActionType.onValueChanges(onData: (_, current) async* {
      removeFieldBlocs(fieldBlocs: [actionSheetColumn]);
      if (current.value == SheetAction.APPEND_ACTION) {
        addFieldBlocs(step: 1, fieldBlocs: [actionSheetColumn]);
      }
    });
  }

  void preFillCommand() {
    List<String> variables = actionSheetColumn.value.map((bloc) => "{" + bloc.value + "}" ).toList();
    String prefill = variables.join(" ");
    triggerCommand.updateValue(prefill);
  }

  @override
  void onSubmitting() async {
    if (state.currentStep == 0) {
      emitSuccess();
    } else if (state.currentStep == 1) {
      // Do not extract variables from message for now.
      //preFillCommand();
      emitSuccess();
    } else if (state.currentStep == 2) {
      dynamic trigger;
      dynamic action;

      switch (actionType.value) {
        default:
          {
            action = new SheetAppendActionModel(
              sheetUrl: actionSheetUrl.value,
              columnValue: actionSheetColumn.value.map((bloc) => bloc.value).toList(),
            );
          }
          break;
      }

      switch (triggerType.value) {
        default:
          {
            trigger = new CommandTriggerModel(command: triggerCommand.value);
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

      emitSuccess(
        successResponse: JsonEncoder.withIndent('  ').convert(
          macro.toJson(),
        ),
      );
    }
  }

  @override
  Future<void> close() {
    List<dynamic> blocs = [macroName, description, actionType, actionSheetUrl, triggerType, triggerCommand] + actionSheetColumn.value.toList();
    blocs.forEach((bloc) {
      bloc.close();
    });

    return super.close();
  }
}

