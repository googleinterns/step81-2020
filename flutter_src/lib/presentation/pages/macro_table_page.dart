import 'package:flutter/material.dart';
import 'package:macrobaseapp/logic/api/firestore_db.dart';
import 'package:macrobaseapp/logic/state/team_state/team_notifier.dart';
import 'package:macrobaseapp/model/entities/team.dart';
import 'package:macrobaseapp/presentation/widgets/misc/no_macro_illustration.dart';
import 'package:macrobaseapp/presentation/widgets/component/macro_table_entry.dart';
import 'package:macrobaseapp/logic/state/macro_state/macro_notifier.dart';
import 'package:macrobaseapp/model/entities/user.dart';
import 'package:provider/provider.dart';

class MacroTable extends StatefulWidget {
  @override
  _MacroTableState createState() => _MacroTableState();
}

class Item {
  Item({
    this.team,
    this.isExpanded = false,
  });

  Team team;
  bool isExpanded;
}

class _MacroTableState extends State<MacroTable> {
  @override
  Widget build(BuildContext context) {
    final macroNotifier = Provider.of<MacroNotifier>(context, listen: false);
    final teamNotifier = Provider.of<TeamNotifier>(context, listen: false);
    final user = Provider.of<User>(context);
    final FirestoreService db = FirestoreService();
    db.getEntries(teamNotifier, macroNotifier, user.email);

    print(teamNotifier.teamList);

    return SingleChildScrollView(
      child: Container(
        child: TeamList(
          teamNotifier: teamNotifier,
        ),
      ),
    );
  }
}

class TeamList extends StatefulWidget {
  final TeamNotifier teamNotifier;

  const TeamList({Key key, this.teamNotifier}) : super(key: key);

  @override
  _TeamListState createState() => _TeamListState(teamNotifier);
}

class _TeamListState extends State<TeamList> {
  @override
  void initState() {
    super.initState();
    _data = teamNotifier.teamList.map((team) => Item(team: team)).toList();
  }

  List<Item> _data;
  final TeamNotifier teamNotifier;

  _TeamListState(this.teamNotifier);

  @override
  Widget build(BuildContext context) {
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
          body: Container(
            child: item.team.macros.length == 0 ? Container() : ListView.builder(
              itemBuilder: (BuildContext context, int index) {
                return MacroTableEntry(macro: item.team.macros[index],);
              },
              itemCount: item.team.macros.length,
            ),
          ),
          isExpanded: item.isExpanded,
        );
      }).toList(),
    );
  }
}
