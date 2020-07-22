import 'dart:html';
import 'package:macrobaseapp/presentation/widgets/button/normal_button.dart';
import 'package:macrobaseapp/presentation/widgets/form/hint_row.dart';

import 'package:flutter/material.dart';
import 'package:flutter_form_bloc/flutter_form_bloc.dart';
import 'package:macrobaseapp/logic/state/macro_bloc.dart';
import 'package:macrobaseapp/logic/state/macro_bloc_template.dart';
import 'package:macrobaseapp/model/entities/action.dart' as entity;
import 'package:macrobaseapp/model/entities/trigger.dart';
import 'package:macrobaseapp/model/entities/user.dart';
import 'package:macrobaseapp/presentation/widgets/form/drop_down_menu.dart';
import 'package:macrobaseapp/presentation/widgets/button/macro_template_button.dart';
import 'package:provider/provider.dart';

class NewMacroForm extends StatefulWidget {
  @override
  _NewMacroFormState createState() => _NewMacroFormState();
}

class _NewMacroFormState extends State<NewMacroForm> {
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
                Scaffold.of(context).showSnackBar(SnackBar(
                  content: Text(state.successResponse),
                  duration: Duration(seconds: 2),
                ));
                // Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (_) => MainNavigator()));
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
                MacroTemplateButton(
                  onPressed: () {
                    FormBlocTemplate.setTemplate(
                        FormBlocTemplate.DAILY_CHECK_IN, wizardFormBloc);
                  },
                  templateName: FormBlocTemplate.DAILY_CHECK_IN,
                  imagePath: "abstract-success.png",
                ),
                MacroTemplateButton(
                  onPressed: () {
                    FormBlocTemplate.setTemplate(
                        FormBlocTemplate.PASS_THE_BATON, wizardFormBloc);
                  },
                  templateName: FormBlocTemplate.PASS_THE_BATON,
                  imagePath: "abstract-no-comments.png",
                ),
                MacroTemplateButton(
                  onPressed: () {
                    FormBlocTemplate.setTemplate(
                        FormBlocTemplate.FROM_SCRATCH, wizardFormBloc);
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
          BlocBuilder<SelectFieldBloc, SelectFieldBlocState>(
              bloc: wizardFormBloc.actionType,
              builder: (context, state) {
                if (state.value == entity.Action.SHEET_ACTION) {
                  return SheetActionForm(wizardFormBloc: wizardFormBloc);
                } else {
                  return Container();
                }
              }),
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

class SheetActionForm extends StatefulWidget {
  final WizardFormBloc wizardFormBloc;
  const SheetActionForm({Key key, this.wizardFormBloc}) : super(key: key);

  @override
  _SheetActionFormState createState() => _SheetActionFormState(wizardFormBloc);
}

class _SheetActionFormState extends State<SheetActionForm> {
  final WizardFormBloc wizardFormBloc;
  _SheetActionFormState(this.wizardFormBloc);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          HintRow(hint: "1. Create an empty Google sheet."),
          HintRow(
            hint:
                "2. Add remindmebot@stepladder-2020.iam.gserviceaccount.com as an Editor",
          ),
          HintRow(hint: "3. Copy in the Sheet URL!"),
          TextFieldBlocBuilder(
            textFieldBloc: wizardFormBloc.actionSheetUrl,
            decoration: InputDecoration(
              labelText: 'The URL for sheet',
              prefixIcon: Icon(Icons.book),
              suffixIcon: InkWell(
                borderRadius: BorderRadius.circular(25),
                child: Icon(Icons.help),
                onTap: () {
                  // TODO
                },
              ),
            ),
          ),
          RadioButtonGroupFieldBlocBuilder(
            selectFieldBloc: wizardFormBloc.sheetActionType,
            itemBuilder: (context, item) => item,
            decoration: InputDecoration(
              labelText: 'Select Sheet Action Type',
              prefixIcon: SizedBox(),
            ),
          ),
          BlocBuilder<SelectFieldBloc, SelectFieldBlocState>(
              bloc: wizardFormBloc.sheetActionType,
              builder: (context, state) {
                if (state.value == entity.SheetAction.APPEND_ACTION){
                  return SheetAppendActionForm(
                    wizardFormBloc: wizardFormBloc,
                  );
                } else {
                  return Container();
                }
              }),
        ],
      ),
    );
  }
}

class SheetAppendActionForm extends StatefulWidget {
  final WizardFormBloc wizardFormBloc;
  const SheetAppendActionForm({Key key, this.wizardFormBloc}) : super(key: key);

  @override
  _SheetAppendActionFormState createState() =>
      _SheetAppendActionFormState(wizardFormBloc);
}

class _SheetAppendActionFormState extends State<SheetAppendActionForm> {
  final WizardFormBloc wizardFormBloc;
  _SheetAppendActionFormState(this.wizardFormBloc);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          Row(
            children: [
              Text(
                "Specify Column Values: ",
                style: Theme.of(context).textTheme.headline6,
              ),
              SizedBox(width: 25),
              NormalButton(
                context: context,
                text: "Add Column",
                onPress: () {
                  wizardFormBloc.actionSheetColumn
                      .addFieldBloc(TextFieldBloc());
                },
              ),
            ],
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
                    return Row(
                      children: [
                        Container(
                          width: 125,
                          child: Text(
                            "Column #" + i.toString() + " is  ",
                            style: Theme.of(context).textTheme.headline6,
                          ),
                        ),
                        DropDownForm(
                          options: entity.AppendAction.VALUE_LIST,
                          bloc: state.fieldBlocs[i],
                        ),
                        Flexible(
                          child: TextFieldBlocBuilder(
                            textFieldBloc: state.fieldBlocs[i],
                            decoration: InputDecoration(
                              suffixIcon: InkWell(
                                  borderRadius: BorderRadius.circular(25),
                                  child: Icon(Icons.delete),
                                  onTap: () {
                                    setState(() {
                                      state.fieldBlocs.removeAt(i);
                                    });
                                  }),
                            ),
                          ),
                        ),
                      ],
                    );
                  },
                );
              }
              return Container();
            },
          )
        ],
      ),
    );
  }
}
