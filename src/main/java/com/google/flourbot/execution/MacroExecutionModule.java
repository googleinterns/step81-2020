package com.google.flourbot.execution;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface MacroExecutionModule {
  public ChatResponse execute(String userEmail, String message, String threadId) throws IOException, GeneralSecurityException;
}
