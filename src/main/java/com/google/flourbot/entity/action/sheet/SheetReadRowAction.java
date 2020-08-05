package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.entity.action.ActionType;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SheetReadRowAction that = (SheetReadRowAction) o;
    return row == that.row &&
        actionType == that.actionType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(actionType, row);
  }
}
