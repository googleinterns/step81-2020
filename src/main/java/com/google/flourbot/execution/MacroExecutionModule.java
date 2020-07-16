package com.google.flourbot.execution;

import java.util.concurrent.ExecutionException;

public interface MacroExecutionModule {
    String execute(String userEmail, String message) throws IllegalStateException, InterruptedException, ExecutionException;
}