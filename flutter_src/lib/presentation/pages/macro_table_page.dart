import 'package:flutter/material.dart';
import 'package:macrobaseapp/logic/api/firestore_db.dart';
import 'package:macrobaseapp/logic/state/team_state/team_notifier.dart';
import 'package:macrobaseapp/presentation/widgets/misc/no_macro_illustration.dart';
import 'package:macrobaseapp/presentation/widgets/component/macro_table_entry.dart';
import 'package:macrobaseapp/logic/state/macro_state/macro_notifier.dart';
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
    final teamNotifier = Provider.of<TeamNotifier>(context, listen: true);
    final user = Provider.of<User>(context);
    final FirestoreService db = FirestoreService();
    db.getEntries(teamNotifier, macroNotifier, user.email);

    if (teamNotifier.teamList.length == 0 &&
        macroNotifier.macroList.length == 0) {
      return NoMacroIllustration();
    } else {
      return Column(children: [
        Container(
          child: ListView.builder(
            itemBuilder: (BuildContext context, int index) {
              return Text(teamNotifier.teamList[index].name);
            },
            itemCount: teamNotifier.teamList.length,
          ),
        ),
        Container(
          child: ListView.builder(
            itemBuilder: (BuildContext context, int index) {
              return MacroTableEntry(
                  macroNotifier: macroNotifier, index: index);
            },
            itemCount: macroNotifier.macroList.length,
          ),
        )
      ]);
    }
  }
}
