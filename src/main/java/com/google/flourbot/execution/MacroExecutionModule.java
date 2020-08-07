package com.google.flourbot.execution;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
* The logic class of the server that interfaces with the database and the sheet to execute
* the macro functionality and prepares contents of a message to be sent back the user
*/
public interface MacroExecutionModule {

  /**
  * Executes the user's desired action and returns a confirmation or failure message to be
  * relayed back to the user through Hangouts Chat

  * @param message the text portion of the message the user typed and sent to the bot
  * @param threadId the thread where the message originated
  * @param roomId the room where the message originated
  * @param messageSenderEmail the email of the user that originally sent the text message
  * @param helpMessage a string to return if the user's messaged asked for help

  * @return a ChatResponse object that will be interpreted in Bot.java and CardResponse.java
  * to produce a text or card message to the user upon executing or failing to execute their
  * instructed action.
  */ 
  ChatResponse getReplyText(String message, String threadId, String roomId, String messageSenderEmail, String helpMessage) throws IOException, GeneralSecurityException;
  
  /**
  * Removes macro from being shared with room
  * @param roomId the room to stop sharing the macro in (roomId is retrieved from user message)
  */
  void removeMacro(String roomId);
}
