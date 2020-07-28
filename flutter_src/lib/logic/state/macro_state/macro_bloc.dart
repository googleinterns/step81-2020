import 'dart:convert';

import 'package:flutter_form_bloc/flutter_form_bloc.dart';
import 'package:macrobaseapp/logic/state/bloc_validator.dart';
import 'package:macrobaseapp/logic/api/firestore_db.dart';
import 'package:macrobaseapp/model/adapters/action_model.dart';
import 'package:macrobaseapp/model/adapters/macro_model.dart';
import 'package:macrobaseapp/model/adapters/trigger_model.dart';
import 'package:macrobaseapp/model/entities/action.dart';
import 'package:macrobaseapp/model/entities/trigger.dart';
import 'package:macrobaseapp/model/entities/user.dart';

class WizardFormBloc extends FormBloc<String, String> {
  final User user;
  final FirestoreService db;

  /*
   Fields that exist for all Macros
   */
  TextFieldBloc macroName = TextFieldBloc(
    asyncValidators: [CustomBlocValidator.nameValidator],
    asyncValidatorDebounceTime: Duration(milliseconds: 300),
    validators: [
      CustomBlocValidator.oneWordValidator,
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
    items: [Action.SHEET_ACTION, Action.ADDRESS_ACTION],
  );

  SelectFieldBloc triggerType = SelectFieldBloc(
    name: 'Trigger Type',
    validators: [
      FieldBlocValidators.required,
    ],
    items: [Trigger.COMMAND_BASED, Trigger.TIME_BASED],
  );

  /*
   Fields for ActionType: Address
   */
  SelectFieldBloc addressType = SelectFieldBloc(name: '', validators: [
    FieldBlocValidators.required,
  ], items: [
    AddressAction.PHYSICAL_ADDRESS,
    AddressAction.WEB_URL
  ]);

  TextFieldBloc address = TextFieldBloc(
      name: 'Address', validators: [FieldBlocValidators.required]);

  /*
   Fields for ActionType: Sheet
   */
  SelectFieldBloc sheetActionType = SelectFieldBloc(
    name: 'Sheet Action Type',
    validators: [
      FieldBlocValidators.required,
    ],
    items: [SheetAction.APPEND_ACTION, SheetAction.BATCH_ACTION],
  );

  TextFieldBloc actionSheetUrl = TextFieldBloc(
    name: "Sheet URL",
    validators: [
      FieldBlocValidators.required,
      CustomBlocValidator.sheetUrlValidator,
    ],
  );

  /*
   Fields for ActionType: Sheet; SheetActionType: Batch;
   */
  SelectFieldBloc batchActionType = SelectFieldBloc(
    name: 'Batch Action Type',
    validators: [
      FieldBlocValidators.required,
    ],
    items: [BatchAction.READ_TYPE, BatchAction.DELETE_TYPE],
  );

  BooleanFieldBloc randomOrder = BooleanFieldBloc();

  /*
   Fields for ActionType: Sheet; SheetActionType: Append;
   */
  ListFieldBloc<TextFieldBloc> actionSheetColumn =
      ListFieldBloc<TextFieldBloc>();

  /*
   Fields for TriggerType: Command
   */
  TextFieldBloc triggerCommand = TextFieldBloc(
    name: 'Command',
    validators: [
      FieldBlocValidators.required,
    ],
  );

  WizardFormBloc({this.user, this.db}) {
    // Default Setup
    addFieldBlocs(
      step: 0,
      fieldBlocs: [macroName, description, scope],
    );
    addFieldBlocs(
      step: 1,
      fieldBlocs: [],
    );
    addFieldBlocs(
      step: 2,
      fieldBlocs: [triggerCommand],
    );
    setupActionType();
    setupSheetActionType();
  }

  void setupActionType() {
    actionType.onValueChanges(onData: (_, current) async* {
      removeFieldBlocs(
        fieldBlocs: [
          addressType,
          address,
          sheetActionType,
          actionSheetUrl,
          batchActionType,
          randomOrder,
          actionSheetColumn
        ],
      );
      switch(current.value) {
        case (Action.ADDRESS_ACTION):
          addFieldBlocs(step: 1, fieldBlocs: [addressType, address]);
          break;
        case (Action.SHEET_ACTION):
          addFieldBlocs(step: 1, fieldBlocs: [sheetActionType, actionSheetUrl]);
          break;
        default:
          break;
      }
    });
  }

  void setupSheetActionType() {
    sheetActionType.onValueChanges(onData: (_, current) async* {
      removeFieldBlocs(
        fieldBlocs: [
          actionSheetColumn,
          batchActionType,
          randomOrder,
        ],
      );
      if (current.value == SheetAction.APPEND_ACTION) {
        addFieldBlocs(step: 1, fieldBlocs: [
          actionSheetColumn,
        ]);
      } else if (current.value == SheetAction.BATCH_ACTION) {
        addFieldBlocs(step: 1, fieldBlocs: [
          batchActionType,
          randomOrder,
        ]);
      }
    });
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
      // preFillCommand();
      emitSuccess();
    } else if (state.currentStep == 2) {
      dynamic trigger;
      dynamic action;

      switch (actionType.value) {
        case Action.ADDRESS_ACTION :
          {
            action = new AddressActionModel(
              addressType: addressType.value,
              address: address.value,
            );
          }
          break;
        case Action.SHEET_ACTION:
          {
            switch (sheetActionType.value) {
              case SheetAction.APPEND_ACTION:
                action = new SheetAppendActionModel(
                  sheetUrl: actionSheetUrl.value,
                  columnValue: actionSheetColumn.value
                      .map((bloc) => bloc.value)
                      .toList(),
                );
                break;
              case SheetAction.BATCH_ACTION:
                action = new SheetBatchActionModel(
                  sheetUrl: actionSheetUrl.value,
                  row: 0,
                  column: 0,
                  batchType: batchActionType.value,
                  randomizeOrder: randomOrder.value,
                );
                break;
              default:
                print(sheetActionType.value);
                throw new Exception([
                  sheetActionType.value +
                      " [sheetActionType] is not implemented!"
                ]);
            }
          }
          break;
        default:
          {
            print(actionType.value);
            throw new Exception(
                [actionType.value + " [actionType] is not implemented!"]);
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

      db.uploadObject('macros', macro.toJson());

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
    actionSheetColumn.close();
    batchActionType.close();
    randomOrder.close();
    triggerType.close();
    triggerCommand.close();
    addressType.close();
    address.close();

    return super.close();
  }
}
