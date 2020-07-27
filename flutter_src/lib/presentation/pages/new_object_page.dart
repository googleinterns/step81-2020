import 'package:flutter/material.dart';
import 'package:macrobaseapp/presentation/pages/new_macro_page.dart';
import 'package:macrobaseapp/presentation/widgets/button/macro_template_button.dart';
import 'package:macrobaseapp/presentation/widgets/form/new_macro_form.dart';

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
    return Column(
      children: [
        MacroTemplateButton(
          templateName: "Create a Team",
          imagePath: "sheet.png",
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => NewMacroPage()),
              );
            }
        ),
        MacroTemplateButton(
            templateName: "Create a Macro",
            imagePath: "sheet.png",
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => NewMacroPage()),
              );
            }
        ),
      ],
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
