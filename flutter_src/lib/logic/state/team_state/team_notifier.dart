import 'dart:collection';
import 'package:flutter/material.dart';
import 'package:macrobaseapp/model/entities/team.dart';

class TeamNotifier with ChangeNotifier {
  List<Team> _teamList = [];

  String userEmail;
  Team _currentTeam;

  UnmodifiableListView<Team> get teamList => UnmodifiableListView(_teamList);

  Team get currentTeam => _currentTeam;

  set teamList(List<Team> teamList) {
    _teamList = teamList;
    notifyListeners();
  }

  set currentTeam(Team team) {
    _currentTeam = team;
    notifyListeners();
  }

  addTeam(Team team) {
    _teamList.insert(0, team);
    notifyListeners();
  }

  deleteTeam(Team team) {
    _teamList.removeWhere((_team) => _team.name  == team.name);
    notifyListeners();
  }
}