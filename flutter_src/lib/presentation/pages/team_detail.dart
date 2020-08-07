import 'dart:convert';
import 'package:macrobaseapp/logic/api/firestore_db.dart';
import 'package:macrobaseapp/logic/state/team_state/team_notifier.dart';
import 'package:macrobaseapp/model/adapters/team_model.dart';

import 'package:flutter/material.dart';
import 'package:macrobaseapp/presentation/widgets/component/macro_table_entry.dart';
import 'package:macrobaseapp/presentation/widgets/misc/no_macro_illustration.dart';
import 'package:provider/provider.dart';

class TeamDetail extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    TeamNotifier teamNotifier =
        Provider.of<TeamNotifier>(context, listen: false);

    final TeamModel teamModel = teamNotifier.currentTeam;
    final json = JsonEncoder.withIndent('  ').convert(teamModel.toJson());

    return Scaffold(
      appBar: AppBar(
        title: Text(teamModel.name),
      ),
      body: teamModel.macros.length == 0
          ? Center(child: NoMacroIllustration())
          : Column(
              children: <Widget>[
                    Text(
                      "Macros of the Team: ",
                      style: Theme.of(context).textTheme.headline4,
                    ),
                  ] +
                  teamModel.macros
                      .map((macro) => MacroTableEntry(
                            macro: macro,
                            onDelete: () {
                              FirestoreService db = FirestoreService();
                              db.removeObject('teams', teamModel.teamId);
                              teamModel.macros.remove(macro);
                              db.uploadObject('teams', teamModel.toJson());

                              Navigator.of(context).pushReplacement(
                                MaterialPageRoute(
                                  builder: (BuildContext context) {
                                    return TeamDetail();
                                  },
                                ),
                              );
                            },
                          ))
                      .toList()),
    );
  }
}
