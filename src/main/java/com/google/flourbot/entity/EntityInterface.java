package com.google.flourbot.entity;

import java.lang.Exception;
import java.util.Optional;
import com.google.flourbot.datastorage.FirebaseDataStorage;

public interface EntityInterface {
    public Optional<Macro> getMacro(String userEmail, String macroName) throws Exception;
}