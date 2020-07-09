import 'package:flutter/material.dart';
import 'package:macrobaseapp/presentation/widgets/macro_table_entry.dart';
import 'package:macrobaseapp/logic/state/macro_notifier.dart';
import 'package:macrobaseapp/logic/usecases/macro_firestore/firestore_macro_operation.dart';
import 'package:macrobaseapp/model/entities/user.dart';
import 'package:macrobaseapp/presentation/pages/macro_detail.dart';
import 'package:provider/provider.dart';

class MacroTable extends StatefulWidget {
  @override
  _MacroTableState createState() => _MacroTableState();
}

class _MacroTableState extends State<MacroTable> {

  @override
  Widget build(BuildContext context) {
    final macroNotifier = Provider.of<MacroNotifier>(context, listen: false);
    final user = Provider.of<User>(context);
    getMacros(macroNotifier, user.email);

    if (macroNotifier.macroList.length == 0) {
      return Container(
        padding: EdgeInsets.all(40),
        child: Column(
          children: <Widget>[
            Image(
              image: AssetImage("empty_macro.png"),
              height: 350,
            ),
            Text(
              "It looks empty here... Create your first macro on the left panel!",
              style: TextStyle(
                fontSize: 20,
                color: Colors.green,
              ),
            )
          ],
        ),
      );
    } else {
      return Container(
        child: ListView.builder(
          itemBuilder: (BuildContext context, int index) {
            return MacroTableEntry(macroNotifier: macroNotifier, index: index);
          },
          itemCount: macroNotifier.macroList.length,
        ),
      );
    }
  }
}
