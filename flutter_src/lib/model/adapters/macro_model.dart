import 'package:macrobaseapp/model/adapters/action_model.dart';
import 'package:macrobaseapp/model/adapters/trigger_model.dart';
import 'package:macrobaseapp/model/entities/macro.dart';
import 'package:meta/meta.dart';

class MacroModel extends Macro {
  MacroModel({
    @required String macroName,
    @required String description,
    @required String creatorId,
    @required dynamic trigger,
    @required dynamic action
  }) : super(
      macroName: macroName,
      description: description,
      creatorId: creatorId,
      trigger: trigger,
      action: action
  );

  factory MacroModel.fromJson(Map<String, dynamic> json) {
    return MacroModel(
      macroName: json['macroName'],
      description: json['description'],
      creatorId: json['creatorId'],
      trigger: TriggerModel.fromJson(json['trigger']),
      action: ActionModel.fromJson(json['action'])
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'macroName': macroName,
      'description': description,
      'creatorId': creatorId,
      'trigger': trigger.toJson(),
      'action': action.toJson(),
    };
  }
}