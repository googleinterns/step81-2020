package com.google.flourbot.execution;

import java.util.concurrent.ExecutionException;

public interface MacroExecutionModule {
    public String execute(String userEmail, String message) throws IllegalStateException, InterruptedException, ExecutionException;
}