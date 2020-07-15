package com.google.flourbot.entity;

import java.util.Map;

public final class Macro {
    private final String creatorId;
    private final String macroName;
    private final Trigger macroTrigger;
    private final Action macroAction;
    
    public Macro (Map<String, Object> document) {
        this.creatorId = document["creatorId"];
        this.macroName = document["macroName"];
        this.macroTrigger = new Trigger(document["trigger"]);
        this.macroAction = new Action(document["action"]);
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

    public String getDocId () {
        return docId;
    }
}