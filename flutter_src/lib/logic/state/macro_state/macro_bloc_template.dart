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
          formBloc.macroName.updateValue("Daily Check-in Bot");
          formBloc.description.updateValue("Daily Check-in Bot records your teams' daily updates in a Google sheet");

          formBloc.actionType.updateValue(Action.SHEET_ACTION);
          formBloc.actionSheetUrl.updateValue("https://docs.google.com/spreadsheets/d/YOUR_SHEET_ID_HERE/edit#gid=0");
        }
        break;
      case PASS_THE_BATON:
        {
          formBloc.macroName.updateValue("Pass-the-Baton Bot");
          formBloc.description.updateValue("Pass-the-Baton Bot randomly orders your team for daily-standup.");

          formBloc.actionType.updateValue(Action.SHEET_ACTION);
          formBloc.actionSheetUrl.updateValue("https://docs.google.com/spreadsheets/d/YOUR_SHEET_ID_HERE/edit#gid=0");
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
