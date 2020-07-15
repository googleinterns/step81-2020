package com.google.flourbot.entity;

import java.util.Map;

public final class Trigger {
    
    private final String triggerCommand;
    private final String triggerType;  

    public Trigger (Map<String, Object> document) {
        this.triggerCommand = (String) document.get("command");
        this.triggerType = (String) document.get("type");
    }

}