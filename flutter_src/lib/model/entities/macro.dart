import 'package:equatable/equatable.dart';
import 'package:meta/meta.dart';

class Macro extends Equatable {

  String macroId;
  final String macroName;
  final String description;
  final String creatorId;
  final dynamic trigger;
  final dynamic action;

  Macro({
    @required this.macroName,
    @required this.description,
    @required this.creatorId,
    @required this.trigger,
    @required this.action,
  });

  @override
  List<Object> get props => [macroName, description, creatorId, trigger, action];
}
