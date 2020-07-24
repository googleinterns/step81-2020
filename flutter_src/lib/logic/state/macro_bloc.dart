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
    asyncValidators: [CustomBlocValidator.nameValidator],
    validators: [
      FieldBlocValidators.required,
    ],
    name: 'Macro Name',
  );

  TextFieldBloc description = TextFieldBloc(
    name: "Description",
    validators: [
      FieldBlocValidators.required,
    ],
  );

  TextFieldBloc scope = TextFieldBloc(name: 'Scope', validators: [
    CustomBlocValidator.commaSeperatedEmailValidator,
  ]);

  SelectFieldBloc actionType = SelectFieldBloc(
    name: 'Action Type',
    validators: [
      FieldBlocValidators.required,
    ],
    items: [Action.SHEET_ACTION],
  );

  SelectFieldBloc sheetActionType = SelectFieldBloc(
    name: 'Sheet Action Type',
    validators: [
      FieldBlocValidators.required,
    ],
    items: [SheetAction.APPEND_ACTION, SheetAction.BATCH_ACTION],
  );

  SelectFieldBloc batchActionType = SelectFieldBloc(
    name: 'Batch Action Type',
    validators: [
      FieldBlocValidators.required,
    ],
    items: [BatchAction.READ_TYPE, BatchAction.DELETE_TYPE],
  );

  TextFieldBloc actionSheetUrl = TextFieldBloc(
    name: "Sheet URL",
    validators: [
      FieldBlocValidators.required,
      CustomBlocValidator.sheetUrlValidator,
    ],
  );

  ListFieldBloc<TextFieldBloc> actionSheetColumn =
      ListFieldBloc<TextFieldBloc>();

  SelectFieldBloc triggerType = SelectFieldBloc(
    name: 'Trigger Type',
    validators: [
      FieldBlocValidators.required,
    ],
    items: [Trigger.COMMAND_BASED, Trigger.TIME_BASED],
  );

  TextFieldBloc triggerCommand = TextFieldBloc(
    name: 'Command',
    validators: [
      FieldBlocValidators.required,
    ],
  );

  WizardFormBloc({this.user}) {
    // Default Setup
    addFieldBlocs(
      step: 0,
      fieldBlocs: [macroName, description, scope],
    );
    addFieldBlocs(
      step: 1,
      fieldBlocs: [sheetActionType, actionSheetUrl, actionSheetColumn, batchActionType],
    );
    addFieldBlocs(
      step: 2,
      fieldBlocs: [triggerCommand],
    );
  }

  void preFillCommand() {
    List<String> variables =
        actionSheetColumn.value.map((bloc) => "{" + bloc.value + "}").toList();
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
        case Action.SHEET_ACTION:
          {
            switch (sheetActionType.value) {
              case SheetAction.APPEND_ACTION:
                action = new SheetAppendActionModel(
                  sheetUrl: actionSheetUrl.value,
                  columnValue:
                  actionSheetColumn.value.map((bloc) => bloc.value).toList(),
                );
                break;
              case SheetAction.BATCH_ACTION:
                action = new SheetBatchActionModel(
                  sheetUrl: actionSheetUrl.value,
                  //TODO Remove hard-coded
                  row: 0,
                  column: "A",
                  batchType: batchActionType.value,
                  randomizeOrder: true,
                );
                break;
              default:
                print(sheetActionType.value);
                throw new Exception([sheetActionType.value + " [sheetActionType] is not implemented!"]);
            }
          }
          break;
        default:
          {
            print(actionType.value);
            throw new Exception([actionType.value + " [actionType] is not implemented!"]);
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
        scope: scope.value.split(",") + [this.user.email],
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
    
    macroName.close();
    description.close();
    actionType.close();
    sheetActionType.close();
    scope.close();
    actionSheetUrl.close();
    triggerType.close();
    triggerCommand.close();
    actionSheetColumn.close();

    return super.close();
  }
}
