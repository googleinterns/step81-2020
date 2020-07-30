package com.google.flourbot.execution;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface MacroExecutionModule {
  ChatResponse getReplyText(String message, String threadId, String roomId, String messageSenderEmail, String helpMessage) throws IOException, GeneralSecurityException;
  void removeMacro(String roomId);
}
