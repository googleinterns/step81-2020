package com.google.flourbot.macro;

public class Macro {
    private String creatorId;
    private String macroName;
    private String docId;
    private Trigger macroTrigger;
    private Action macroAction;
    
    public Macro (String creatorId, String macroName, String docId, Trigger macroTrigger, Action macroAction) {
        this.creatorId = creatorId;
        this.macroName = macroName;
        this.docId = docId;
        this.macroTrigger = macroTrigger;
        this.macroAction = macroAction;
    }

    public String creatorId () {
        return creatorId;
    }

    public String getMacroName () {
        return macroName;
    }

    public String getDocId () {
        return docId;
    }
}