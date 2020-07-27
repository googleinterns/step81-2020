import 'package:flutter/cupertino.dart';
import 'package:macrobaseapp/model/adapters/macro_model.dart';
import 'package:macrobaseapp/model/entities/team.dart';

class TeamModel extends Team {
  TeamModel(
      {@required name,
      @required iconUrl,
      @required description,
      @required macros})
      : super(name, iconUrl, description, macros);

  factory TeamModel.fromJson(Map<String, dynamic> json) {
    return TeamModel(
      name: json["name"],
      iconUrl: json["iconUrl"],
      description: json["description"],
      macros: json["macros"]
          .map((macroJson) => MacroModel.fromJson(macroJson))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      "name": name,
      "iconUrl": iconUrl,
      "description": description,
      "macros": macros
          .cast<MacroModel>()
          .map((macroObject) => macroObject.toJson())
          .toList()
    };
  }
}
