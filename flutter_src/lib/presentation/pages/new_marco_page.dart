import 'package:flutter/material.dart';
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
      child: Column(
        children: <Widget>[
          Container(
            margin: EdgeInsets.symmetric(vertical: 20.0),
            height: 200.0,
            child: ListView(
              // This next line does the trick.
              scrollDirection: Axis.horizontal,
              children: <Widget>[
                macro_template_button(templateName: "Daily Check-in Macro",),
                macro_template_button(templateName: "Create from Scratch",),
              ],
            ),
          ),
          WizardForm(),
        ],
      ),
    );
  }
}
