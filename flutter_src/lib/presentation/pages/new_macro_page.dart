import 'package:flutter/material.dart';
import 'package:macrobaseapp/presentation/widgets/form/new_macro_form.dart';

class NewMacroPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Second Route"),
      ),
      body: SingleChildScrollView(
          child: Container(
            padding: EdgeInsets.all(40),
            child: Column(
              children: <Widget>[
                NewMacroForm(),
              ],
            ),
          ),
        ),
      );
  }
}
