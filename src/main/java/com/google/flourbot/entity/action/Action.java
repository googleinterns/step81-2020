package com.google.flourbot.entity.action;

import java.util.Map;

public abstract class Action {
    public enum ActionType { SHEET_APPEND }
    public abstract ActionType getActionType();
}

