package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.entity.action.ActionType;

public final class SheetReadRowAction extends SheetAction {

  private final ActionType actionType = ActionType.SHEET_READ_ROW;
  private final int row;

  public SheetReadRowAction(String sheetUrl, int row) {
    super(sheetUrl);
    this.row = row;
  }

  public final ActionType getActionType() {
    return actionType;
  }

  public final int getRow() {
    return row;
  }

  public final boolean getSelectRandomEntry() {
    return selectRandomEntry;
  }
}
