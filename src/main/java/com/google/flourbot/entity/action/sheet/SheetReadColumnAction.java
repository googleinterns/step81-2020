package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.api.DriveCloudSheet;
import com.google.flourbot.entity.action.ActionType;

public final class SheetReadColumnAction extends SheetAction {

  private final ActionType actionType = ActionType.SHEET_READ_COLUMN;
  private final String column;
  private final boolean selectRandomEntry;

  public SheetReadColumnAction(String sheetUrl, String column, boolean selectRandomEntry) {
    super(sheetUrl);
    this.column = column;
    this.selectRandomEntry = selectRandomEntry;
  }

  public SheetReadColumnAction(String sheetUrl, int column, boolean selectRandomEntry) {
    // Overload
    super(sheetUrl);
    this.column = DriveCloudSheet.toAlphabetic(column + 1);
    this.selectRandomEntry = selectRandomEntry;
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
