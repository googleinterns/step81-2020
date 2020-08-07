package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.entity.action.ActionType;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SheetReadColumnAction that = (SheetReadColumnAction) o;
    return actionType == that.actionType &&
        Objects.equals(column, that.column);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actionType, column);
  }
}

