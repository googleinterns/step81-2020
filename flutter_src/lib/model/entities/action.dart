import 'package:equatable/equatable.dart';
import 'package:meta/meta.dart';

class Action extends Equatable {
  static const String SHEET_ACTION = "Sheet Action";
  static const String ADDRESS_ACTION = "Address Action";

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

class AddressAction extends Action {
  static const String WEB_URL = "Redirect Url";
  static const String PHYSICAL_ADDRESS = "Address";

  final String addressType;
  final String address;

  AddressAction(@required this.addressType, @required this.address)
      : super(Action.ADDRESS_ACTION);

  @override
  List<Object> get props => super.props..addAll([address]);
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
  static const List<String> VALUE_LIST = [VALUE_EMPTY, VALUE_TIME, VALUE_EMAIL, VALUE_CONTENT];
  static const VALUE_EMPTY = "EMPTY";
  static const VALUE_TIME = "TIME";
  static const VALUE_EMAIL = "EMAIL";
  static const VALUE_CONTENT = "CONTENT";

  final List<String> columnValue;

  AppendAction(
    sheetUrl,
    this.columnValue,
  ) : super(sheetUrl: sheetUrl, sheetAction: SheetAction.APPEND_ACTION);

  @override
  List<Object> get props => super.props..addAll([columnValue]);
}
