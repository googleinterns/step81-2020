import 'package:macrobaseapp/logic/state/macro_bloc.dart';
import 'package:macrobaseapp/model/entities/action.dart';

class FormBlocTemplate {
  static const String DAILY_CHECK_IN = "Daily Check-in";
  static const String FROM_SCRATCH = "Create From Scratch";

  static void setTemplate(String templateName, WizardFormBloc formBloc) {
    switch (templateName) {
      case DAILY_CHECK_IN:
        {
          formBloc.macroName.updateValue("Daily Check-in Bot");
          formBloc.description.updateValue("Daily Check-in Bot records your teams' daily updates in a Google sheet");

          formBloc.actionType.updateValue(Action.SHEET_ACTION);
          formBloc.actionSheetUrl.updateValue("COPY IN YOUR GOOGLE SHEET URL HERE");
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
