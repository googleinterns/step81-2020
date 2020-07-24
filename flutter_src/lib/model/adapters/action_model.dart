import 'package:macrobaseapp/model/entities/action.dart';
import 'package:meta/meta.dart';

abstract class ActionModel extends Action {
  ActionModel({
    @required String actionType,
  }) : super(actionType);

  static fromJson(Map<String, dynamic> json) {
    if (json['type'] == Action.SHEET_ACTION) {
      return SheetActionModel.fromJson(json);
    }
  }
}

abstract class SheetActionModel extends SheetAction {
  SheetActionModel({
    @required sheetUrl,
    @required sheetAction,
  }) : super(sheetUrl: sheetUrl, sheetAction: sheetAction);

  static fromJson(Map<String, dynamic> json) {
    if (json['sheetAction'] == SheetAction.APPEND_ACTION) {
      return SheetAppendActionModel.fromJson(json);
    }
  }
}

class SheetAppendActionModel extends AppendAction {
  SheetAppendActionModel({
    @required sheetUrl,
    @required List<String> columnValue,
  }) : super(sheetUrl, columnValue);

  factory SheetAppendActionModel.fromJson(Map<String, dynamic> json) {
    return SheetAppendActionModel(
      sheetUrl: json["sheetUrl"],
      columnValue: json["columnValue"].cast<String>(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      "type": type,
      "sheetUrl": sheetUrl,
      "sheetAction": SheetAction.APPEND_ACTION,
      "columnValue": columnValue,
    };
  }
}
