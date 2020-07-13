package com.google.flourbot.datastorage;

public class Macro {
    public String creatorId;
    public String macroName;
    public String docId;
    
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
}