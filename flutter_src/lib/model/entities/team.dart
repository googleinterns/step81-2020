import 'package:equatable/equatable.dart';

class Team extends Equatable {

  final String name;
  final String iconUrl;
  final String description;

  Team(this.name, this.iconUrl, this.description);

  @override
  List<Object> get props => [name, iconUrl, description];

}