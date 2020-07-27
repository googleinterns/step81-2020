package com.google.flourbot.execution;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface MacroExecutionModule {
  String execute(String userEmail, String macroCreatorEmail, String message, String threadId, String macroName) throws IOException, GeneralSecurityException;
}
