import 'dart:convert';
import 'dart:html';

import 'package:flutter_form_bloc/flutter_form_bloc.dart';
import 'package:macrobaseapp/logic/api/firestore_db.dart';
import 'package:macrobaseapp/logic/state/bloc_validator.dart';
import 'package:macrobaseapp/model/adapters/action_model.dart';
import 'package:macrobaseapp/model/adapters/macro_model.dart';
import 'package:macrobaseapp/model/adapters/team_model.dart';
import 'package:macrobaseapp/model/adapters/trigger_model.dart';
import 'package:macrobaseapp/model/entities/action.dart';
import 'package:macrobaseapp/model/entities/trigger.dart';
import 'package:macrobaseapp/model/entities/user.dart';

class TeamFormBloc extends FormBloc<String, String> {
  final FirestoreService db;
  final User user;

  TextFieldBloc teamName = TextFieldBloc(
    name: "Team Name",
    validators: [
      FieldBlocValidators.required,
    ],
  );

  TextFieldBloc iconUrl = TextFieldBloc(
    name: "Icon URL (Optional)",
    validators: [
      (string) => CustomBlocValidator.urlValidator(string)
          ? null
          : "Icon URL is not a valid link to an logo",
    ],
  );

  TextFieldBloc description = TextFieldBloc(
    name: "Description",
    validators: [
      FieldBlocValidators.required,
    ],
  );

  TeamFormBloc({this.user, this.db}) {
    addFieldBlocs(fieldBlocs: [teamName, iconUrl, description]);
  }

  @override
  void onSubmitting() async {
    final team = TeamModel(
        name: teamName.value.trim(),
        iconUrl: iconUrl.value.trim(),
        description: description.value.trim(),
        creatorId: this.user.email,
        macros: []);

    db.uploadObject('teams', team.toJson());

    emitSuccess(
      successResponse: JsonEncoder.withIndent('  ').convert(
        team.toJson(),
      ),
    );
  }

  @override
  Future<void> close() {
    teamName.close();
    iconUrl.close();
    description.close();

    return super.close();
  }
}
