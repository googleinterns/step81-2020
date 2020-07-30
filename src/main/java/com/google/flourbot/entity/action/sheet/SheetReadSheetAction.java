package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.api.DriveCloudSheet;
import com.google.flourbot.entity.action.ActionType;

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

  public final boolean getSelectRandomEntry() {
    return selectRandomEntry;
  }
}
