import 'package:flutter/cupertino.dart';
import 'package:macrobaseapp/model/adapters/macro_model.dart';
import 'package:macrobaseapp/model/entities/team.dart';

class TeamModel extends Team {
  TeamModel(
      {@required name,
      @required iconUrl,
      @required description,
      @required macros,
      @required creatorId})
      : super(
            name: name,
            iconUrl: iconUrl,
            description: description,
            macros: macros,
            creatorId: creatorId);

  factory TeamModel.fromJson(Map<String, dynamic> json) {
    return TeamModel(
        name: json["name"],
        iconUrl: json["iconUrl"],
        description: json["description"],
        macros: json["macros"]
            .map((macroJson) => MacroModel.fromJson(macroJson))
            .toList().cast<MacroModel>(),
        creatorId: json["creatorId"]);
  }

  Map<String, dynamic> toJson() {
    return {
      "name": name,
      "iconUrl": iconUrl,
      "description": description,
      "macros": macros == null
          ? []
          : macros
              .cast<MacroModel>()
              .map((macroObject) => macroObject.toJson())
              .toList(),
      "creatorId": creatorId,
    };
  }
}
