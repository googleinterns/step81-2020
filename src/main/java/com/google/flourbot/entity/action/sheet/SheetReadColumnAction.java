package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.api.DriveCloudSheet;
import com.google.flourbot.entity.action.ActionType;

public final class SheetReadColumnAction extends SheetAction {

  private final ActionType actionType = ActionType.SHEET_READ_COLUMN;
  private final String column;
  private final SelectionMethod selectionMethod;

  public SheetReadColumnAction(String sheetUrl, String column, SelectionMethod selectionMethod) {
    super(sheetUrl);
    this.column = column;
    this.selectionMethod = selectionMethod;
  }

  public SheetReadColumnAction(String sheetUrl, int column, SelectionMethod selectionMethod) {
    // Overload
    super(sheetUrl);
    this.column = DriveCloudSheet.toAlphabetic(column + 1);
    this.selectionMethod = selectionMethod;
  }

  public final ActionType getActionType() {
    return actionType;
  }

  public final String getColumn() {
    return column;
  }

  public final SelectionMethod getSelectionMethod() {
    return selectionMethod;
  }
}
