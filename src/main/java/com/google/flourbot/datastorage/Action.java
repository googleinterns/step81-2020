package com.google.flourbot.datastorage;

public class Action {

    public String[] columnValue;
    public String sheetAction;
    public String sheetUrl;
    public String actionType;

    public Action (String[] columnValue, String sheetAction, String sheetUrl, String actionType) {
        this.columnValue = columnValue;
        this.sheetAction = sheetAction;
        this.sheetUrl = sheetUrl;
        this.actionType = actionType;
    }

}