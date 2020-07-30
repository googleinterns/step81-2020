package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.entity.action.ActionType;

public final class SheetReadRowAction extends SheetAction {

  private final ActionType actionType = ActionType.SHEET_READ_ROW;
  private final int row;
  private final SelectionMethod selectionMethod;

  public SheetReadRowAction(String sheetUrl, int row, SelectionMethod selectionMethod) {
    super(sheetUrl);
    this.row = row;
    this.selectionMethod = selectionMethod;
  }

  public final ActionType getActionType() {
    return actionType;
  }

  public final int getRow() {
    return row;
  }

  public final SelectionMethod getSelectionMethod() {
    return selectionMethod;
  }
}
