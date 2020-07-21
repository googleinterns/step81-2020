import 'package:flutter/material.dart';
import 'package:macrobaseapp/presentation/widgets/new_macro_form.dart';

// Define a custom Form widget.
class NewMacroPage extends StatefulWidget {
  @override
  NewMacroPageState createState() {
    return NewMacroPageState();
  }
}

class NewMacroPageState extends State<NewMacroPage> {
  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Container(
        padding: EdgeInsets.all(40),
        child: Column(
          children: <Widget>[
            NewMacroForm(),
          ],
        ),
      ),
    );
  }
}
