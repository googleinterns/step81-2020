import 'package:flutter/material.dart';
import 'package:macrobaseapp/logic/state/macro_bloc_template.dart';
import 'package:macrobaseapp/presentation/widgets/macro_template_button.dart';
import 'package:macrobaseapp/model/entities/user.dart';
import 'package:macrobaseapp/presentation/widgets/new_macro_form.dart';

// Define a custom Form widget.
class MyCustomForm extends StatefulWidget {
  @override
  MyCustomFormState createState() {
    return MyCustomFormState();
  }
}

class MyCustomFormState extends State<MyCustomForm> {
  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Container(
        padding: EdgeInsets.all(40),
        child: Column(
          children: <Widget>[
            WizardForm(),
          ],
        ),
      ),
    );
  }
}
