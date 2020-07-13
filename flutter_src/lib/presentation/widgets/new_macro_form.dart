import 'dart:html';

import 'package:flutter/material.dart';
import 'package:flutter_form_bloc/flutter_form_bloc.dart';
import 'package:macrobaseapp/logic/state/macro_bloc.dart';
import 'package:macrobaseapp/logic/state/macro_bloc_template.dart';
import 'package:macrobaseapp/model/entities/action.dart' as entity;
import 'package:macrobaseapp/model/entities/trigger.dart';
import 'package:macrobaseapp/model/entities/user.dart';
import 'package:macrobaseapp/presentation/navigation/main_navigator.dart';
import 'package:macrobaseapp/presentation/widgets/macro_template_button.dart';
import 'package:provider/provider.dart';

class WizardForm extends StatefulWidget {
  @override
  _WizardFormState createState() => _WizardFormState();
}

class _WizardFormState extends State<WizardForm> {
  var _type = StepperType.vertical;

  @override
  Widget build(BuildContext context) {
    final User user = Provider.of<User>(context);

    return BlocProvider(
      create: (context) => WizardFormBloc(user: user),
      child: Builder(builder: (context) {
        return Theme(
          data: Theme.of(context).copyWith(
            inputDecorationTheme: InputDecorationTheme(
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(20),
              ),
            ),
          ),
          child: FormBlocListener<WizardFormBloc, String, String>(
            onSubmitting: (context, state) => LoadingDialog.show(context),
            onSuccess: (context, state) {
              LoadingDialog.hide(context);

              if (state.stepCompleted == state.lastStep) {
                Navigator.of(context).pushReplacement(
                    MaterialPageRoute(builder: (_) => MainNavigator()));
              }
            },
            onFailure: (context, state) {
              LoadingDialog.hide(context);
            },
            child: StepperFormBlocBuilder<WizardFormBloc>(
              type: _type,
              physics: ClampingScrollPhysics(),
              stepsBuilder: (formBloc) {
                return [
                  _infoStep(formBloc),
                  _actionStep(formBloc),
                  _triggerStep(formBloc),
                ];
              },
            ),
          ),
        );
      }),
    );
  }

  FormBlocStep _infoStep(WizardFormBloc wizardFormBloc) {
    return FormBlocStep(
      title: Text(
        'Choose a Template',
        style: Theme.of(context).textTheme.headline4,
      ),
      content: Column(
        children: <Widget>[
          Container(
            margin: EdgeInsets.symmetric(vertical: 20.0),
            height: 200.0,
            child: ListView(
              // This next line does the trick.
              scrollDirection: Axis.horizontal,
              children: <Widget>[
                macro_template_button(
                  onPressed: () {
                    FormBlocTemplate.setTemplate(FormBlocTemplate.DAILY_CHECK_IN, wizardFormBloc);
                  },
                  templateName: FormBlocTemplate.DAILY_CHECK_IN,
                  imagePath: "abstract-success.png",
                ),
                macro_template_button(
                  onPressed: () {
                    FormBlocTemplate.setTemplate(FormBlocTemplate.FROM_SCRATCH, wizardFormBloc);
                  },
                  templateName: FormBlocTemplate.FROM_SCRATCH,
                  imagePath: "having-job.png",
                ),
              ],
            ),
          ),
          TextFieldBlocBuilder(
            textFieldBloc: wizardFormBloc.macroName,
            suffixButton: SuffixButton.asyncValidating,
            decoration: InputDecoration(
              labelText: 'Macro Name',
              prefixIcon: Icon(Icons.android),
            ),
          ),
          TextFieldBlocBuilder(
            textFieldBloc: wizardFormBloc.description,
            decoration: InputDecoration(
              labelText: 'One line description of the macro',
              prefixIcon: Icon(Icons.book),
            ),
          ),
        ],
      ),
    );
  }

  FormBlocStep _actionStep(WizardFormBloc wizardFormBloc) {
    return FormBlocStep(
      title: Text(
        'Actions',
        style: Theme.of(context).textTheme.headline4,
      ),
      content: Column(
        children: <Widget>[
          Row(
            children: <Widget>[
              FlatButton(
                onPressed: () {
                  wizardFormBloc.actionType
                      .updateValue(entity.Action.SHEET_ACTION);
                },
                padding: EdgeInsets.all(0.0),
                child: Image(
                  image: AssetImage("sheet.png"),
                  height: 100,
                  width: 200,
                ),
              ),
            ],
          ),
          TextFieldBlocBuilder(
            textFieldBloc: wizardFormBloc.actionSheetUrl,
            decoration: InputDecoration(
              labelText: 'The URL for sheet',
              prefixIcon: Icon(Icons.book),
            ),
          ),
          RaisedButton(
            onPressed: () {
              wizardFormBloc.actionSheetColumn.addFieldBloc(TextFieldBloc());
            },
            child: Text("Add Column",
              style: Theme.of(context).textTheme.button,
            )
          ),
          BlocBuilder<ListFieldBloc<TextFieldBloc>,
              ListFieldBlocState<TextFieldBloc>>(
            bloc: wizardFormBloc.actionSheetColumn,
            builder: (context, state) {
              if (state.fieldBlocs.isNotEmpty) {
                return ListView.builder(
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  itemCount: state.fieldBlocs.length,
                  itemBuilder: (context, i) {
                    return TextFieldBlocBuilder(
                      textFieldBloc: state.fieldBlocs[i],
                    );
                  },
                );
              }
              return Container();
            },
          ),
        ],
      ),
    );
  }

  FormBlocStep _triggerStep(WizardFormBloc wizardFormBloc) {
    return FormBlocStep(
      title: Text(
        'Trigger',
        style: Theme.of(context).textTheme.headline4,
      ),
      content: Column(
        children: <Widget>[
          Row(
            children: <Widget>[
              FlatButton(
                onPressed: () {
                  wizardFormBloc.triggerType.updateValue(Trigger.COMMAND_BASED);
                },
                padding: EdgeInsets.all(0.0),
                child: Column(children: <Widget>[
                  Image(
                    image: AssetImage("hangout.png"),
                    height: 100,
                    width: 200,
                  ),
                  Text(
                    "Command Based",
                    style: Theme.of(context).textTheme.caption,
                  ),
                ]),
              ),
            ],
          ),
          TextFieldBlocBuilder(
            textFieldBloc: wizardFormBloc.triggerCommand,
            decoration: InputDecoration(
              labelText: 'Command to trigger the macro',
              prefixIcon: Icon(Icons.insert_comment),
            ),
          ),
        ],
      ),
    );
  }
}

class LoadingDialog extends StatelessWidget {
  static void show(BuildContext context, {Key key}) => showDialog<void>(
        context: context,
        useRootNavigator: false,
        barrierDismissible: false,
        builder: (_) => LoadingDialog(key: key),
      ).then((_) => FocusScope.of(context).requestFocus(FocusNode()));

  static void hide(BuildContext context) => Navigator.pop(context);

  LoadingDialog({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async => false,
      child: Center(
        child: Card(
          child: Container(
            width: 80,
            height: 80,
            padding: EdgeInsets.all(12.0),
            child: CircularProgressIndicator(),
          ),
        ),
      ),
    );
  }
}
