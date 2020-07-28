import 'package:flutter_form_bloc/flutter_form_bloc.dart';
import 'package:macrobaseapp/logic/api/firestore_db.dart';
import 'package:macrobaseapp/logic/state/bloc_validator.dart';
import 'package:macrobaseapp/model/adapters/team_model.dart';

class TeamFormBloc extends FormBloc<String, String> {
  final FirestoreService db;

  TextFieldBloc teamName = TextFieldBloc(
    name: "Team Name",
    validators: [
      FieldBlocValidators.required,
    ],
  );

  TextFieldBloc iconUrl = TextFieldBloc(
    name: "Icon URL (Optional)",
    validators: [
      CustomBlocValidator.urlValidator,
    ],
  );

  TextFieldBloc description = TextFieldBloc(
    name: "Description",
    validators: [
      FieldBlocValidators.required,
    ],
  );

  TeamFormBloc({this.db}) {
    addFieldBlocs(fieldBlocs: [teamName, iconUrl, description]);
  }

  @override
  void onSubmitting() async {
    final team = TeamModel(
      name: teamName.value.trim(),
      iconUrl: iconUrl.value.trim(),
      description: description.value.trim(),
    );

    db.uploadObject('teams', team.toJson());
  }

  @override
  Future<void> close() {

    teamName.close();
    iconUrl.close();
    description.close();

    return super.close();
  }
}
