package com.google.flourbot.entity.action;

public interface Action {
  public ActionType getActionType();
  public String getSheetId();
  public String getSheetUrl();
}
