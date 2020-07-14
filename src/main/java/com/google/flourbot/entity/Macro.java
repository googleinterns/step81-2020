package com.google.flourbot.entity;

public final class Macro {
    private final String creatorId;
    private final String macroName;
    private final String docId;
    private final Trigger macroTrigger;
    private final Action macroAction;
    
    public Macro (String creatorId, String macroName, String docId, Object macroTrigger, Object macroAction) {
        this.creatorId = creatorId;
        this.macroName = macroName;
        this.docId = docId;
        this.macroTrigger = macroTrigger;
        this.macroAction = macroAction;
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