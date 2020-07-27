import 'package:flutter_form_bloc/flutter_form_bloc.dart';
import 'package:macrobaseapp/logic/state/bloc_validator.dart';
import 'package:macrobaseapp/model/adapters/team_model.dart';

class TeamFormBloc extends FormBloc<String, String> {

  TextFieldBloc teamName = TextFieldBloc(
    name: "Description",
    validators: [
      FieldBlocValidators.required,
    ],
  );

  TextFieldBloc iconUrl = TextFieldBloc(
    name: "Icon URL (Optional)",
    validators: [
      CustomBlocValidator.UrlValidator,
    ],
  );

  TextFieldBloc description = TextFieldBloc(
    name: "Description",
    validators: [
      FieldBlocValidators.required,
    ],
  );

  TeamFormBloc() {
    addFieldBlocs(fieldBlocs: [teamName, iconUrl, description]);
  }

  @override
  void onSubmitting() async {
    final team = TeamModel(
      name: teamName.value.trim(),
      iconUrl: iconUrl.value.trim(),
      description: description.value.trim(),
    );

    //TODO
    // Upload Team Object
  }

  @override
  Future<void> close() {

    teamName.close();
    iconUrl.close();
    description.close();

    return super.close();
  }
}
