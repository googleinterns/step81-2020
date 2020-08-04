package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.entity.action.ActionType;

public final class SheetReadColumnAction extends SheetAction {

  private final ActionType actionType = ActionType.SHEET_READ_COLUMN;
  private final String column;

  public SheetReadColumnAction(String sheetUrl, String column) {
    super(sheetUrl);
    this.column = column;
  }

  public final ActionType getActionType() {
    return actionType;
  }

  public final String getColumn() {
    return column;
  }
}

