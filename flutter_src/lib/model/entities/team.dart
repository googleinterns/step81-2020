import 'package:equatable/equatable.dart';
import 'package:macrobaseapp/model/entities/macro.dart';

class Team extends Equatable {

  final String name;
  final String iconUrl;
  final String description;
  final String creatorId;

  final List<Macro> macros;

  Team(this.name, this.iconUrl, this.description, this.macros, this.creatorId);

  @override
  List<Object> get props => [name, iconUrl, description];

}