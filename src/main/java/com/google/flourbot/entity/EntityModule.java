package com.google.flourbot.entity;

import java.lang.Exception;
import java.util.Optional;
import com.google.flourbot.datastorage.FirebaseDataStorage;

import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;

public interface EntityModule {
    public Optional<Macro> getMacro(String userEmail, String macroName) throws InterruptedException, ExecutionException;
}