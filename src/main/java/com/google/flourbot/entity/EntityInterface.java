package com.google.flourbot.entity;

import java.lang.Exception;
import com.google.flourbot.datastorage.FirebaseDataStorage;

public interface EntityInterface {
    public Macro getMacro(String userEmail, String macroName) throws Exception;
}