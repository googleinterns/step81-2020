package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.api.DriveCloudSheet;
import com.google.flourbot.entity.action.ActionType;

public final class SheetReadColumnAction extends SheetAction {

  private final ActionType actionType = ActionType.SHEET_READ_COLUMN;
  private final String column;

  public SheetReadColumnAction(String sheetUrl, String column) {
    super(sheetUrl);
    this.column = column;
  }

  public SheetReadColumnAction(String sheetUrl, int column) {
    // Overload
    super(sheetUrl);
    this.column = DriveCloudSheet.toAlphabetic(column + 1);
  }

  public final ActionType getActionType() {
    return actionType;
  }

  public final String getColumn() {
    return column;
  }

  public final boolean getSelectRandomEntry() {
    return selectRandomEntry;
  }
}
