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
      child: Container(
        padding: EdgeInsets.all(40),
        child: Column(
          children: <Widget>[
            Align(
              alignment: Alignment.centerLeft,
              child: Text(
                "Choose a Template:",
                style: Theme.of(context).textTheme.headline4,
              ),
            ),
            Container(
              margin: EdgeInsets.symmetric(vertical: 20.0),
              height: 200.0,
              child: ListView(
                // This next line does the trick.
                scrollDirection: Axis.horizontal,
                children: <Widget>[
                  macro_template_button(
                    templateName: "Daily Check-in",
                    imagePath: "abstract-success.png",
                  ),
                  macro_template_button(
                    templateName: "Create from Scratch",
                    imagePath: "having-job.png",
                  ),
                ],
              ),
            ),
            WizardForm(),
          ],
        ),
      ),
    );
  }
}
