package com.google.flourbot.entity;

import java.util.Map;

public final class Action {

    private final String[] columnValue; 
    private final String sheetAction;
    private final String sheetUrl;
    private final String actionType;

    public Action (Map<String, Object> document) {
        this.columnValue = (String[]) document.get("columnValue");
        this.sheetAction = (String) document.get("sheetAction");
        this.sheetUrl = (String) document.get("sheetUrl");
        this.actionType = (String) document.get("type");
    }

    public String[] getColumnValue() {
        return columnValue;
    }

    public String getSheetAction() {
        return sheetAction;
    }

    public String getSheetUrl() {
        return sheetUrl;
    }

    public String getActionType() {
        return actionType;
    }
}