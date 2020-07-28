package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.api.DriveCloudSheet;
import com.google.flourbot.entity.action.ActionType;

public final class SheetReadSheetAction extends SheetAction {

  private final ActionType actionType = ActionType.SHEET_READ_SHEET;
  private final String sheetName;
  private final boolean selectRandomEntry;

  public SheetReadSheetAction(String sheetUrl, String sheetName, boolean selectRandomEntry) {
    super(sheetUrl);
    this.sheetName = sheetName;
    this.selectRandomEntry = selectRandomEntry;
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
