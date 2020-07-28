package com.google.flourbot.api;

import com.google.flourbot.execution.MacroExecutionModule;
import com.google.flourbot.execution.MacroExecutionModuleImplementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.chat.v1.HangoutsChat;
import com.google.api.services.chat.v1.model.Message;
import com.google.api.services.chat.v1.model.Thread;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Logger;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

@SpringBootApplication
@RestController
public class Bot {

  static final String CHAT_SCOPE = "https://www.googleapis.com/auth/chat.bot";
  private static final String SERVICE_ACCOUNT = "/service-acct.json";
  private static final Logger logger = Logger.getLogger(Bot.class.getName());
  private static String helpMessage;
  private String macroCreatorEmail;
  private String replyText;
  private Map<String, Map<String, String>> roomToMacro = new HashMap<String, Map<String, String>>();

  private static MacroExecutionModule macroExecutionModule;

  public static void main(String[] args) {
    macroExecutionModule = MacroExecutionModuleImplementation.initializeServer();
    SpringApplication.run(Bot.class, args);
  }

  /**
    * Handles a GET request to the /bot endpoint.
    *
    * @param event Event from chat.
    * @return Message
    */
  @PostMapping("/")
  public void onEvent(@RequestBody JsonNode event) throws IOException, GeneralSecurityException {
    switch (event.at("/type").asText()) {

      case "ADDED_TO_SPACE":
        String spaceType = event.at("/space/type").asText();
        if ("ROOM".equals(spaceType)) {
          String displayName = event.at("/space/displayName").asText();
          replyText = String.format("Thanks for adding me to %s. Type \"@MacroBot help\" at any time to see my instructions.", displayName);
          helpMessage = "To use a room member's macro, the creator of a macro must write \"@MacroBot initiate MacroName \". " + 
            "Once this has been sent, any room member can use the initialized macro by writing \"@MacroBot MacroName <your message> \" " + 
            "If your macro has already been used in a thread, you may omit the MacroName and can simply write \"@MacroBot <your message>\".";

        } else {
          String displayName = event.at("/user/displayName").asText();
          replyText = String.format("Thanks for adding me to a DM, %s! Type \"@MacroBot help\" at any time to see my instructions.", displayName);
          helpMessage = "To use one of your macros, simply write \"@MacroBot MacroName <your message> \".";
        }
        break;

      case "MESSAGE":
        String message = event.at("/message/text").asText();
        String[] words = message.split(" ");
        String threadId = event.at("/message/thread/name").asText();

        // Check that a message was written.
        if (words.length <= 1) {
            replyText = "You must type a message when you message me. Please type \"@MacroBot help\" for more instructions.";
            break;
        }
        // Reply to the help message.
        if (words[1].equalsIgnoreCase("help")) {  
            replyText = helpMessage; 
        }        
        else if (words.length >= 2) {
            // If a macro is being initialized for a room.
            // TODO: handle cases where a user tries to initialize a macro they don't have 
            if (words[1].equalsIgnoreCase("initiate")) {
                // getMacroName() requires the macroName to be the second word of the message, so remove the word "initiate". This will put the macro name as the second word in the message.
                List<String> wordsWithoutInitiate = new LinkedList(Arrays.asList(words));
                wordsWithoutInitiate.remove("initiate");
                String newMessage = String.join(" ", wordsWithoutInitiate);
                String macroName = MacroExecutionModuleImplementation.getMacroName(newMessage, threadId);

                macroCreatorEmail = event.at("/message/sender/email").asText();
                Map<String, String> macroToCreator = new HashMap<String, String> ();
                macroToCreator.put(macroName, macroCreatorEmail);

                roomToMacro.put(event.at("/space/name").asText(), macroToCreator);
                replyText = String.format("The %s macro belonging to %s has been initiated for this room. All users in this room can use this macro.", macroName, macroCreatorEmail);
            }
            // If someone is trying to use the macro
            else if (roomToMacro.containsKey(event.at("/space/name").asText())) {
                String macroName = MacroExecutionModuleImplementation.getMacroName(message, threadId);
                Map<String, String> macroToCreator = roomToMacro.get(event.at("/space/name").asText());
                macroCreatorEmail = macroToCreator.get(macroName);
                String userEmail = event.at("/message/sender/email").asText();
                replyText = macroExecutionModule.execute(userEmail, macroCreatorEmail, message, threadId, macroName);
            }  
            else {
                replyText = "You do not have any macros initialized for this room.";
            } 
        }
        else {
            replyText = "That is not a valid message.";
        }
        break;

      case "REMOVED_FROM_SPACE":
        roomToMacro.remove(event.at("/space/name").asText());
        logger.info("Bot removed from space.");
        break;

      default:
        throw new IllegalArgumentException(event.at("/type").asText());
    }

    if (replyText.isEmpty()) {
        throw new IllegalArgumentException();
    }

    // [START async-response]
    
    // Set up credentials
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    GoogleCredentials credentials = GoogleCredentials.fromStream(
            Bot.class.getResourceAsStream(SERVICE_ACCOUNT)
    ).createScoped(CHAT_SCOPE);
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

    // Create chat service
    HangoutsChat chatService = new HangoutsChat.Builder(
            httpTransport,
            jsonFactory,
            requestInitializer)
          .setApplicationName("bot-chat")
          .build();

    // Generate and send request to post in chat room
    String spaceName = event.at("/space/name").asText();
    Message reply = new Message().setText(replyText);
    // If replying to a message, set thread name to keep conversation together
    if (event.has("message")) {
      String threadName = event.at("/message/thread/name").asText();
      Thread thread = new Thread().setName(threadName);
      reply.setThread(thread);
    }

    chatService.spaces().messages().create(spaceName, reply).execute();
    // [END async-response]
  }
}
