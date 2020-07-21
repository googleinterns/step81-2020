import 'package:flutter/material.dart';
import 'package:macrobaseapp/presentation/widgets/no_macro_illustration.dart';
import 'package:macrobaseapp/presentation/widgets/macro_table_entry.dart';
import 'package:macrobaseapp/logic/state/macro_notifier.dart';
import 'package:macrobaseapp/logic/usecases/macro_firestore/firestore_macro_operation.dart';
import 'package:macrobaseapp/model/entities/user.dart';
import 'package:provider/provider.dart';

class MacroTable extends StatefulWidget {
  @override
  _MacroTableState createState() => _MacroTableState();
}

class _MacroTableState extends State<MacroTable> {
  @override
  Widget build(BuildContext context) {
    final macroNotifier = Provider.of<MacroNotifier>(context, listen: true);
    final user = Provider.of<User>(context);
    getMacros(macroNotifier, user.email);

    if (macroNotifier.macroList.length == 0) {
      return NoMacroIllustration();
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
