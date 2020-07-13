package com.google.flourbot.datastorage;

public interface DataStorage {
    Macro getMacro (String userEmail, String macroName) throws Exception; 
}