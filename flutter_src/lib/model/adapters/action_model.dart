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
    else if (json['sheetAction'] == SheetAction.BATCH_ACTION) {
      return SheetBatchActionModel.fromJson(json);
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

class SheetBatchActionModel extends BatchAction {
  SheetBatchActionModel({
    @required sheetUrl,
    row,
    column,
    batchType,
    randomizeOrder,
  }) : super(sheetUrl, row, column, batchType, randomizeOrder);

  factory SheetBatchActionModel.fromJson(Map<String, dynamic> json) {
    return SheetBatchActionModel(
      sheetUrl: json["sheetUrl"],
      row: json["row"],
      column: json["column"],
      batchType: json["batchType"],
      randomizeOrder: json["randomizeOrder"]
    );
  }

  Map<String, dynamic> toJson() {
    return {
      "type": type,
      "sheetAction": sheetAction,
      "sheetUrl": sheetUrl,
      "row": row,
      "column": column,
      "batchType": batchType,
      "randomizeOrder": randomizeOrder
    };
  }
}
