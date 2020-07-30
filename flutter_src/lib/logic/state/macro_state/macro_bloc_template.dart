import 'package:flutter_form_bloc/flutter_form_bloc.dart';
import 'package:macrobaseapp/logic/state/macro_state/macro_bloc.dart';
import 'package:macrobaseapp/model/entities/action.dart';

class FormBlocTemplate {
  static const String DAILY_CHECK_IN = "Daily Check-in";
  static const String FROM_SCRATCH = "Create From Scratch";
  static const String PASS_THE_BATON = "Pass-the-Baton";

  static void setTemplate(String templateName, WizardFormBloc formBloc) {
    switch (templateName) {
      case DAILY_CHECK_IN:
        {
          formBloc.macroName.updateValue("DailyBot");
          formBloc.description.updateValue(
              "DailyBot records your teams' daily updates in a Google sheet");

          formBloc.actionType.updateValue(Action.SHEET_ACTION);
          formBloc.sheetActionType.updateValue(SheetAction.APPEND_ACTION);
          formBloc.actionSheetUrl.updateValue(
              "https://docs.google.com/spreadsheets/d/YOUR_SHEET_ID_HERE/edit#gid=0");

          formBloc.actionSheetColumn.addFieldBloc(TextFieldBloc());
          formBloc.actionSheetColumn.addFieldBloc(TextFieldBloc());
          formBloc.actionSheetColumn.addFieldBloc(TextFieldBloc());

          formBloc.actionSheetColumn.value[0].updateValue(AppendAction.VALUE_TIME);
          formBloc.actionSheetColumn.value[1].updateValue(AppendAction.VALUE_EMAIL);
          formBloc.actionSheetColumn.value[2].updateValue(AppendAction.VALUE_CONTENT);
        }
        break;
      case PASS_THE_BATON:
        {
          formBloc.macroName.updateValue("BatonBot");
          formBloc.description.updateValue(
              "Pass-the-Baton Bot randomly orders your team for daily-standup.");

          formBloc.actionType.updateValue(Action.SHEET_ACTION);
          formBloc.sheetActionType.updateValue(SheetAction.BATCH_ACTION);
          formBloc.batchActionType.updateValue(BatchAction.READ_TYPE);

          formBloc.actionSheetUrl.updateValue(
              "https://docs.google.com/spreadsheets/d/YOUR_SHEET_ID_HERE/edit#gid=0");
          formBloc.randomOrder.updateValue(true);
        }
        break;
      case FROM_SCRATCH:
        {
          clearFields(formBloc);
        }
        break;
      default:
        {
          clearFields(formBloc);
        }
        break;
    }
  }

  static void clearFields(WizardFormBloc formBloc) {
    formBloc.macroName.updateValue("");
    formBloc.description.updateValue("");
    formBloc.actionSheetUrl.updateValue("");
    formBloc.triggerCommand.updateValue("");
  }
}
