package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.entity.action.ActionType;

import java.util.Arrays;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SheetAppendRowAction that = (SheetAppendRowAction) o;
    return actionType == that.actionType &&
        Arrays.equals(columnValue, that.columnValue);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(actionType);
    result = 31 * result + Arrays.hashCode(columnValue);
    return result;
  }
}
