import 'package:flutter/material.dart';
import 'package:macrobaseapp/logic/state/macro_notifier.dart';
import 'package:macrobaseapp/logic/usecases/macro_firestore/firestore_macro_operation.dart';
import 'package:macrobaseapp/presentation/pages/macro_detail.dart';

class MacroTableEntry extends StatelessWidget {
  const MacroTableEntry({
    Key key,
    @required this.macroNotifier,
    @required this.index,
  }) : super(key: key);

  final MacroNotifier macroNotifier;
  final int index;

  @override
  Widget build(BuildContext context) {
    return Card(
      child: ListTile(
        leading: FlutterLogo(size: 72.0),
        title: Text(macroNotifier.macroList[index].macroName),
        subtitle: Text(macroNotifier.macroList[index].description),
        trailing: IconButton(
          icon: Icon(Icons.delete),
          onPressed: () {
            removeMacro(macroNotifier.macroList[index].macroId);
            macroNotifier.deleteMacro(macroNotifier.macroList[index]);
          },
        ),
        onTap: () {
          macroNotifier.currentMacro = macroNotifier.macroList[index];
          Navigator.of(context).push(
            MaterialPageRoute(
              builder: (BuildContext context) {
                return MacroDetail();
              },
            ),
          );
        },
      ),
    );
  }
}
