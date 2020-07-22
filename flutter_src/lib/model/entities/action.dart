import 'package:equatable/equatable.dart';
import 'package:meta/meta.dart';

class Action extends Equatable {
  static const String SHEET_ACTION = "Sheet Action";
  static const String POLL_ACTION = "Poll Action";

  final String type;

  Action(this.type);

  @override
  List<Object> get props => [type];
}


// Defines the operation to append a row to a Google Sheet
class SheetAction extends Action {
  static const String APPEND_ACTION = "Append Action";
  static const String READ_ACTION = "Read Action";

  final String sheetUrl;
  final String sheetAction;

  SheetAction({
    @required this.sheetUrl,
    @required this.sheetAction,
  }) : super(Action.SHEET_ACTION);

  @override
  List<Object> get props => super.props..addAll([sheetUrl, sheetAction]);
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


// Defines a Poll action configuration
class PollAction extends Action {
  final String question;
  final List<String> choices;
  final bool userCanAddOptions;
  final bool userCanVoteMultiple;

  PollAction({
    @required this.question,
    @required this.choices,
    @required this.userCanAddOptions,
    @required this.userCanVoteMultiple,
  }) : super(Action.POLL_ACTION);

  @override
  List<Object> get props => super.props
    ..addAll([question, choices, userCanAddOptions, userCanVoteMultiple]);
}
