package com.google.flourbot.api;

import com.google.flourbot.execution.MacroExecutionModule;
import com.google.flourbot.execution.MacroExecutionModuleImplementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RestController
public class Bot {
  static final String CHAT_SCOPE = "https://www.googleapis.com/auth/chat.bot";
  private static final String SERVICE_ACCOUNT = "/service-acct.json";
  private static final Logger logger = Logger.getLogger(Bot.class.getName());

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
    String replyText = "";
    switch (event.at("/type").asText()) {
      case "ADDED_TO_SPACE":
        String spaceType = event.at("/space/type").asText();
        if ("ROOM".equals(spaceType)) {
          String displayName = event.at("/space/displayName").asText();
          replyText = String.format("Thanks for adding me to %s", displayName);
        } else {
          String displayName = event.at("/user/displayName").asText();
          replyText = String.format("Thanks for adding me to a DM, %s!", displayName);
        }
        break;
      case "MESSAGE":
        String email = event.at("/message/sender/email").asText();
        String message = event.at("/message/text").asText();
        // Sends request to execution module
        replyText = macroExecutionModule.execute(email, message);
        break;
      case "REMOVED_FROM_SPACE":
        String name = event.at("/space/name").asText();
        logger.info(String.format("Bot removed from %s", name));
        break;
      default:
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
