import 'package:flutter/material.dart';
import 'package:macrobaseapp/logic/api/firestore_db.dart';
import 'package:macrobaseapp/logic/state/team_state/team_notifier.dart';
import 'package:macrobaseapp/model/entities/macro.dart';
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

class _MacroTableState extends State<MacroTable> {
  @override
  Widget build(BuildContext context) {
    MacroNotifier macroNotifier =
        Provider.of<MacroNotifier>(context, listen: true);
    TeamNotifier teamNotifier =
        Provider.of<TeamNotifier>(context, listen: true);

    final user = Provider.of<User>(context);

    final FirestoreService db = FirestoreService();
    db.getEntries(teamNotifier, macroNotifier, user.email);

    return ListView(
      children: [
        Text(
          "Teams: ",
          style: Theme.of(context).textTheme.headline4,
        ),
        Container(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            crossAxisAlignment: CrossAxisAlignment.start,
            children:
            teamNotifier.teamList.length == 0 ? Center(child: Text("No Team has been built yet")) :
            teamNotifier.teamList
                .map((emoji) => _buildDragTarget(emoji))
                .toList(),
          ),
        ),
        Text(
          "Stand-alone Macros: ",
          style: Theme.of(context).textTheme.headline4,
        ),
        Container(
          child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              crossAxisAlignment: CrossAxisAlignment.end,
              children:
              macroNotifier.macroList.length == 0 ? NoMacroIllustration() :
              macroNotifier.macroList.map((macro) {
                return Draggable<Macro>(
                  data: macro,
                  child: Material(
                    child: ConstrainedBox(
                        constraints: BoxConstraints(
                            maxWidth: MediaQuery.of(context).size.width),
                        child: MacroTableEntry(
                          macro: macro,
                        )),
                  ),
                  feedback: Material(
                    child: ConstrainedBox(
                        constraints: BoxConstraints(
                            maxWidth: MediaQuery.of(context).size.width),
                        child: MacroTableEntry(
                          macro: macro,
                        )),
                  ),
                  childWhenDragging: Container(),
                );
              }).toList()),
        ),
      ],
    );
  }

  Widget _buildDragTarget(Team emoji) {
    return DragTarget<Macro>(
      builder: (BuildContext context, List<Macro> incoming, List rejected) {
        return Card(
          child: ListTile(
            leading: FlutterLogo(size: 56.0),
            title: Text(emoji.name),
            subtitle: Text(emoji.description),
            trailing: Icon(Icons.more_vert),
          ),
        );
      },
      onWillAccept: (data) => true,
      onAccept: (data) {
        Scaffold.of(context).showSnackBar(SnackBar(
            content: Text(data.macroName + " added to " + emoji.name + "!")));
      },
      onLeave: (data) {},
    );
  }
}