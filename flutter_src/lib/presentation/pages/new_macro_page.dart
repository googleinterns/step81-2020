import 'package:flutter/material.dart';
import 'package:macrobaseapp/presentation/widgets/form/new_macro_form.dart';

class NewMacroPage extends StatelessWidget {
  final Widget form;

  const NewMacroPage({Key key, this.form}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Creation Form"),
      ),
      body: SingleChildScrollView(
        child: form,
      ),
    );
  }
}
