package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.action.ActionType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SheetAction implements Action {

  protected final String sheetUrl;

  public SheetAction( String sheetUrl) {
    this.sheetUrl = sheetUrl;
  }

  public abstract ActionType getActionType();

  public String getSheetUrl() {
    return sheetUrl;
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
