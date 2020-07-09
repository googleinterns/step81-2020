import 'package:meta/meta.dart';
import 'package:macrobaseapp/model/entities/trigger.dart';

class TriggerModel extends Trigger {
  TriggerModel({
    @required String type,
  }) : super(type);

  static fromJson(Map<String, dynamic> json) {
    if (json['type'] == Trigger.COMMAND_BASED) {
      return CommandTriggerModel.fromJson(json);
    } else {
    }
  }
}

class CommandTriggerModel extends CommandTrigger {
  CommandTriggerModel({
    @required String command,
  }) : super(command: command);

  factory CommandTriggerModel.fromJson(Map<String, dynamic> json) {
    return CommandTriggerModel(
      command: json['command'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      "type": Trigger.COMMAND_BASED,
      "command": command,
    };
  }
}
