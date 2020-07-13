package com.google.flourbot.macro_classes;

public class Macro {
    public String creatorId;
    public String macroName;
    public String docId;
    public Trigger macroTrigger;
    public Action macroAction;
    
    public Macro (String creatorId, String macroName, String docId) {
        this.creatorId = creatorId;
        this.macroName = macroName;
        this.docId = docId;
    }

    public void setCreatorId (String creatorId) {
        this.creatorId = creatorId;
    }

    public void setMacroName (String macroName) {
        this.macroName = macroName;
    }

    public void setDocId (String docId) {
        this.docId = docId;
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

    public void createMacroAction (String[] columnValue, String sheetAction, String sheetUrl, String actionType) {
        macroAction = new Action(columnValue, sheetAction, sheetUrl, actionType);
    }

    public void createMacroTrigger (String triggerCommand, String triggerType) {
        macroTrigger = new Trigger (triggerCommand, triggerType);
    }
}