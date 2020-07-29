import 'package:equatable/equatable.dart';
import 'package:flutter/material.dart';
import 'package:macrobaseapp/model/entities/macro.dart';

class Team extends Equatable {
  String teamId;
  final String name;
  final String iconUrl;
  final String description;
  final String creatorId;

  final List<Macro> macros;

  Team(
      {@required this.name,
      @required this.iconUrl,
      @required this.description,
      @required this.macros,
      @required this.creatorId,
      this.teamId});

  @override
  List<Object> get props => [name, iconUrl, description];
}
