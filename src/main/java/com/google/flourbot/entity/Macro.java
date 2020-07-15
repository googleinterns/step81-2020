package com.google.flourbot.entity;

import java.util.Map;
import java.lang.Object;

public final class Macro {
    private final String creatorId;
    private final String macroName;
    private final Trigger macroTrigger;
    private final Action macroAction;
    
    public Macro (Map<String, Object> document) {
        this.creatorId = (String) document.get("creatorId");
        this.macroName = (String) document.get("macroName");
        this.macroTrigger = new Trigger((Map<String, Object>) document.get("trigger"));
        this.macroAction = new Action((Map<String, Object>) document.get("action"));
    }

    public Action getAction() {
        return macroAction;
    }

    public String getCreatorId () {
        return creatorId;
    }

    public String getMacroName () {
        return macroName;
    }
}