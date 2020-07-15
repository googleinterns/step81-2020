package com.google.flourbot.entity;

import java.util.Map;

public final class Action {

    private final String[] columnValue; 
    private final String sheetAction;
    private final String sheetUrl;
    private final String actionType;

    public Action (Map<String, Object> document) {
        this.columnValue = document["columnValue"];
        this.sheetAction = document["sheetAction"];
        this.sheetUrl = document["sheetUrl"];
        this.actionType = document["type"];
    }

}