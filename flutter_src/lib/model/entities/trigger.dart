import 'package:equatable/equatable.dart';
import 'package:meta/meta.dart';

class Trigger extends Equatable {
  static const String COMMAND_BASED = "Command Trigger";
  static const String TIME_BASED = "Time Trigger";

  final String type;

  Trigger(this.type);

  @override
  List<Object> get props => [type];
}

class CommandTrigger extends Trigger {
  final String command;

  CommandTrigger({
    @required this.command,
  }) : super(Trigger.COMMAND_BASED);

  @override
  List<Object> get props => super.props..addAll([command]);
}

class TimeTrigger extends Trigger {
  final DateTime baseTime;
  final int stride;
  final String unit;

  TimeTrigger({
    @required this.baseTime,
    @required this.stride,
    @required this.unit,
  }) : super(Trigger.TIME_BASED);

  @override
  List<Object> get props => super.props..addAll([baseTime, stride, unit]);
}