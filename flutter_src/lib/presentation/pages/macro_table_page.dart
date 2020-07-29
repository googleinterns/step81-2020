import 'package:flutter/material.dart';
import 'package:macrobaseapp/logic/api/firestore_db.dart';
import 'package:macrobaseapp/logic/state/team_state/team_notifier.dart';
import 'package:macrobaseapp/model/entities/team.dart';
import 'package:macrobaseapp/presentation/widgets/misc/no_macro_illustration.dart';
import 'package:macrobaseapp/presentation/widgets/component/macro_table_entry.dart';
import 'package:macrobaseapp/logic/state/macro_state/macro_notifier.dart';
import 'package:macrobaseapp/model/entities/user.dart';
import 'package:provider/provider.dart';

import '../../logic/state/macro_state/macro_notifier.dart';
import '../../model/entities/team.dart';

class MacroTable extends StatefulWidget {
  @override
  _MacroTableState createState() => _MacroTableState();
}

class Item {
  Item({
    this.team,
    this.isExpanded = true,
  });

  Team team;
  bool isExpanded;
}

class _MacroTableState extends State<MacroTable> {
  List<Item> _data =
      TeamNotifier().teamList.map((team) => Item(team: team)).toList();

  @override
  Widget build(BuildContext context) {
    MacroNotifier macroNotifier =
        Provider.of<MacroNotifier>(context, listen: true);
    TeamNotifier teamNotifier =
        Provider.of<TeamNotifier>(context, listen: true);

    final user = Provider.of<User>(context);

    final FirestoreService db = FirestoreService();
    db.getEntries(teamNotifier, macroNotifier, user.email);

    return Column(
        children: [_teamList(), _macroList(macroNotifier)]);
  }

  Widget _macroList(MacroNotifier macroNotifier) {
    if (macroNotifier.macroList.length == 0) {
      return NoMacroIllustration();
    } else {
      return Expanded(
        child: ListView.builder(
          itemBuilder: (BuildContext context, int index) {
            return MacroTableEntry(macro: macroNotifier.macroList[index]);
          },
          itemCount: macroNotifier.macroList.length,
        ),
      );
    }
  }

  Widget _teamList() {
    return ExpansionPanelList(
      expansionCallback: (int index, bool isExpanded) {
        setState(() {
          _data[index].isExpanded = !isExpanded;
        });
      },
      children: _data.map<ExpansionPanel>((Item item) {
        return ExpansionPanel(
          headerBuilder: (BuildContext context, bool isExpanded) {
            return ListTile(
              title: Text(item.team.name),
            );
          },
          body: item.team.macros.length == 0
              ? Text("No Macros for this Team yet...")
              : Column(
                  children: item.team.macros
                      .map((macro) => new MacroTableEntry(
                            macro: macro,
                          ))
                      .toList()),
          isExpanded: item.isExpanded,
        );
      }).toList(),
    );
  }
}
