import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_form_bloc/flutter_form_bloc.dart';
import 'package:macrobaseapp/logic/api/firestore_db.dart';
import 'package:macrobaseapp/logic/state/macro_state/macro_notifier.dart';
import 'package:macrobaseapp/logic/state/team_state/team_notifier.dart';
import 'package:macrobaseapp/model/entities/macro.dart';
import 'package:macrobaseapp/presentation/pages/macro_detail.dart';
import 'package:macrobaseapp/presentation/widgets/form/drop_down_menu.dart';
import 'package:provider/provider.dart';

class MacroTableEntry extends StatefulWidget {
  final Macro macro;

  const MacroTableEntry({Key key, this.macro})
      : super(key: key);
  @override
  _MacroTableEntryState createState() =>
      _MacroTableEntryState(macro);
}

class _MacroTableEntryState extends State<MacroTableEntry> {
  final Macro macro;
  _MacroTableEntryState(this.macro);

  @override
  Widget build(BuildContext context) {
    return Card(
      child: ListTile(
        leading: FlutterLogo(size: 72.0),
        title: Text(macro.macroName),
        subtitle: Text(macro.description),
        trailing: IconButton(icon: Icon(Icons.delete), onPressed: () {
          FirestoreService db = FirestoreService();
          db.removeObject('macros', macro.macroId);
          MacroNotifier().macroList.remove(macro);
        }),
        onTap: () {
          MacroNotifier().currentMacro = macro;
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

