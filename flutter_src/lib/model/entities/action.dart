import 'package:equatable/equatable.dart';
import 'package:meta/meta.dart';

class Action extends Equatable {
  static const String SHEET_ACTION = "Sheet Action";
  final String type;

  Action(this.type);

  @override
  List<Object> get props => [type];
}

// Defines the operation to append a row to a Google Sheet
class SheetAction extends Action {
  static const String APPEND_ACTION = "Append Action";
  static const String BATCH_ACTION = "Batch Action";

  final String sheetUrl;
  final String sheetAction;

  SheetAction({
    @required this.sheetUrl,
    @required this.sheetAction,
  }) : super(Action.SHEET_ACTION);

  @override
  List<Object> get props => super.props..addAll([sheetUrl, sheetAction]);
}

class BatchAction extends SheetAction {
  static const String READ_TYPE = "Read";
  static const String DELETE_TYPE = "Delete";

  int row = null;
  int column = null;

  String batchType;
  bool randomizeOrder = false;

  BatchAction(
    sheetUrl,
    this.row,
    this.column,
    this.batchType,
    this.randomizeOrder,
  ) : super(sheetUrl: sheetUrl, sheetAction: SheetAction.BATCH_ACTION);
  
  @override
  List<Object> get props => super.props..addAll([row, column, randomizeOrder]);
}

class AppendAction extends SheetAction {
  // TODO: ENUM
  static const List<String> VALUE_LIST = ["EMPTY", "TIME", "EMAIL", "CONTENT"];

  final List<String> columnValue;

  AppendAction(
    sheetUrl,
    this.columnValue,
  ) : super(sheetUrl: sheetUrl, sheetAction: SheetAction.APPEND_ACTION);

  @override
  List<Object> get props => super.props..addAll([columnValue]);
}
