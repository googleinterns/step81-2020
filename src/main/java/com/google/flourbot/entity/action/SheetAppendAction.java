package com.google.flourbot.entity.action;

import java.util.Map;
import com.google.flourbot.entity.action.Action;


public final class SheetAppendAction extends Action {

    private final ActionType actionType = Action.SHEET_APPEND;

    private final String[] columnValue; 
    private final String sheetAction;
    private final String sheetUrl;

    public Action (String[] columnValue, String sheetAction, String sheetUrl) {
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