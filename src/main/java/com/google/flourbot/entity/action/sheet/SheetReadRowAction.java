package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.action.ActionType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SheetReadRowAction extends Action {

  private final ActionType actionType = ActionType.SHEET_READ_ROW;

  private final SheetEntryType[] columnValue;
  private final String sheetAction;
  private final String sheetUrl;

  public SheetReadRowAction(SheetEntryType[] columnValue, String sheetAction, String sheetUrl) {
    this.columnValue = columnValue;
    this.sheetAction = sheetAction;
    this.sheetUrl = sheetUrl;
  }

  public SheetEntryType[] getColumnValue() {
    return columnValue;
  }

  public String getSheetAction() {
    return sheetAction;
  }

  public String getSheetUrl() {
    return sheetUrl;
  }

  public ActionType getActionType() {
    return actionType;
  }

  public String getSheetId() {
    String sheetId = "";
    Pattern pattern = Pattern.compile("(d\\/[a-zA-Z0-9-_]+)");
    Matcher matcher = pattern.matcher(this.sheetUrl);
    // Take the first occurance
    if (matcher.find()) {
      sheetId = matcher.group(0).substring(2);
    }
    return sheetId;
  }
}
