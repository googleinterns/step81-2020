package com.google.flourbot.entity;

import java.util.Optional;
import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;

public interface EntityModule {
    Optional<Macro> getMacro(String userEmail, String macroName) throws InterruptedException, ExecutionException;
}