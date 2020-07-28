import 'package:flutter/material.dart';
import 'package:macrobaseapp/presentation/pages/new_macro_page.dart';
import 'package:macrobaseapp/presentation/widgets/button/wide_button.dart';
import 'package:macrobaseapp/presentation/widgets/form/new_macro_form.dart';
import 'package:macrobaseapp/presentation/widgets/form/new_team_from.dart';

// Define a custom Form widget.
class NewObjectPage extends StatefulWidget {
  @override
  NewObjectPageState createState() {
    return NewObjectPageState();
  }
}

class NewObjectPageState extends State<NewObjectPage> {
  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          WideButton(
              templateName: "Create a New Team",
              imagePath: "data-exchange.png",
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => NewMacroPage(form: NewTeamForm(),)),
                );
              }),
          WideButton(
              templateName: "Create a New Macro",
              imagePath: "abstract-coffee-break.png",
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => NewMacroPage(form: NewMacroForm())),
                );
              }),
        ],
      ),],
    );
  }
}

//class NewMacroPageState extends State<NewMacroPage> {
//  @override
//  Widget build(BuildContext context) {
//    return SingleChildScrollView(
//      child: Container(
//        padding: EdgeInsets.all(40),
//        child: Column(
//          children: <Widget>[
//            NewMacroForm(),
//          ],
//        ),
//      ),
//    );
//  }
//}
