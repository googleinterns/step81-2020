import 'package:flutter/material.dart';
import 'package:macrobaseapp/logic/api/firestore_db.dart';
import 'package:macrobaseapp/logic/state/macro_state/macro_notifier.dart';
import 'package:macrobaseapp/model/entities/macro.dart';
import 'package:macrobaseapp/presentation/pages/macro_detail.dart';
import 'package:provider/provider.dart';

class MacroTableEntry extends StatelessWidget {
  const MacroTableEntry({
    Key key,
    @required this.macro, this.onDelete, this.onClick,
  }) : super(key: key);

  final Macro macro;
  final Function onDelete;
  final Function onClick;

  @override
  Widget build(BuildContext context) {
    return Card(
      child: ListTile(
        leading: FlutterLogo(size: 72.0),
        title: Text(macro.macroName),
        subtitle: Text(macro.description),
        trailing: IconButton(
          icon: Icon(Icons.delete),
          onPressed: () {
            FirestoreService db = FirestoreService();
            db.removeObject('macros', macro.macroId);
//            macroNotifier.deleteMacro(macroNotifier.macroList[index]);
          },
        ),
        onTap: () {
//          macroNotifier.currentMacro = macroNotifier.macroList[index];
//          Navigator.of(context).push(
//            MaterialPageRoute(
//              builder: (BuildContext context) {
//                return MacroDetail();
//              },
//            ),
//          );
        },
      ),
    );
  }
}
