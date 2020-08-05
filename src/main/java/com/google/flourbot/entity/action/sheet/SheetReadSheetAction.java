package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.api.DriveCloudSheet;
import com.google.flourbot.entity.action.ActionType;

import java.util.Objects;

public final class SheetReadSheetAction extends SheetAction {

  private final ActionType actionType = ActionType.SHEET_READ_SHEET;
  private final String sheetName;

  public SheetReadSheetAction(String sheetUrl, String sheetName) {
    super(sheetUrl);
    this.sheetName = sheetName;
  }

  public final ActionType getActionType() {
    return actionType;
  }

  public final String getSheetName() {
    return sheetName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SheetReadSheetAction that = (SheetReadSheetAction) o;
    return actionType == that.actionType &&
        sheetName.equals(that.sheetName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actionType, sheetName);
  }
}
