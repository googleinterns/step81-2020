import 'package:flutter/material.dart';
import 'package:flutter_form_bloc/flutter_form_bloc.dart';
import 'package:macrobaseapp/logic/api/firestore_db.dart';
import 'package:macrobaseapp/logic/state/loading_dialog.dart';
import 'package:macrobaseapp/logic/state/team_state/team_bloc.dart';
import 'package:macrobaseapp/presentation/navigation/main_navigator.dart';
import 'package:provider/provider.dart';

class NewTeamForm extends StatefulWidget {
  @override
  _NewTeamFormState createState() => _NewTeamFormState();
}

class _NewTeamFormState extends State<NewTeamForm> {
  @override
  Widget build(BuildContext context) {
    final FirestoreService db = FirestoreService();

    return BlocProvider(
        create: (context) => TeamFormBloc(db: db),
        child: Builder(builder: (context) {
          final teamFormBloc = context.bloc<TeamFormBloc>();

          return Theme(
            data: Theme.of(context).copyWith(
              inputDecorationTheme: InputDecorationTheme(
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(20),
                ),
              ),
            ),
            child: FormBlocListener<TeamFormBloc, String, String>(
              onSubmitting: (context, state) => LoadingDialog.show(context),
              onSuccess: (context, state) {
                LoadingDialog.hide(context);
                Navigator.of(context).pushReplacement(
                    MaterialPageRoute(builder: (_) => MainNavigator()));
              },
              onFailure: (context, state) {
                LoadingDialog.hide(context);
              },
              child: SingleChildScrollView(
                child: Column(
                  children: [
                    TextFieldBlocBuilder(
                      textFieldBloc: teamFormBloc.teamName,
                      decoration: InputDecoration(
                        labelText: 'Team Name',
                        prefixIcon: Icon(Icons.people),
                      ),
                    ),
                    TextFieldBlocBuilder(
                      textFieldBloc: teamFormBloc.description,
                      decoration: InputDecoration(
                        labelText: 'Description',
                        prefixIcon: Icon(Icons.description),
                      ),
                    ),
                    TextFieldBlocBuilder(
                      textFieldBloc: teamFormBloc.iconUrl,
                      decoration: InputDecoration(
                        labelText: "Team Icon URL",
                        prefixIcon: Icon(Icons.image),
                      ),
                    ),
                    RaisedButton(
                      onPressed: teamFormBloc.submit,
                      child: Text("Sumbit"),
                    )
                  ],
                ),
              ),
            ),
          );
        }));
  }
}
