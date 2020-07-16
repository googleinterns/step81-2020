package com.google.flourbot.entity;

import java.util.Map;
import java.lang.Object;

import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.trigger.Trigger;

public final class Macro {
    private final String creatorId;
    private final String macroName;
    private final Trigger macroTrigger;
    private final Action macroAction;
    
    public Macro (String creatorId, String macroName, Trigger macroTrigger, Action macroAction) {
        this.creatorId = creatorId;
        this.macroName = macroName;
        this.macroTrigger = macroTrigger;
        this.macroAction = macroAction;
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