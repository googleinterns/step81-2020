package com.google.flourbot.entity.action;

public final class SheetAppendAction extends Action {

    private final ActionType actionType = ActionType.SHEET_APPEND;

    private final String[] columnValue; 
    private final String sheetAction;
    private final String sheetUrl;

    public SheetAppendAction(String[] columnValue, String sheetAction, String sheetUrl) {
        this.columnValue = columnValue;
        this.sheetAction = sheetAction;
        this.sheetUrl = sheetUrl;
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

    public ActionType getActionType() {
        return actionType;
    }
}