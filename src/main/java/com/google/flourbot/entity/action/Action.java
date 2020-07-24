package com.google.flourbot.entity.action;

public abstract class Action {
  public abstract ActionType getActionType();
  public abstract String getSheetId();
  public abstract String getSheetUrl();
}
