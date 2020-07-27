package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.entity.action.ActionType;

public final class SheetAppendRowAction extends SheetAction {

  private final ActionType actionType = ActionType.SHEET_APPEND_ROW;
  private final SheetEntryType[] columnValue;

  public SheetAppendRowAction(String sheetUrl, SheetEntryType[] columnValue) {
    super(sheetUrl);
    this.columnValue = columnValue;
  }

  public final ActionType getActionType() {
    return actionType;
  }

  public final SheetEntryType[] getColumnValue() {
    return columnValue;
  }
}
