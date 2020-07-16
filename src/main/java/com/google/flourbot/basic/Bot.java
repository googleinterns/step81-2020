/**
 * Copyright 2017 Google Inc.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.flourbot.basic;

// [START async-bot]

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.chat.v1.HangoutsChat;
import com.google.api.services.chat.v1.model.Message;
import com.google.api.services.chat.v1.model.Thread;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class Bot {
  static final String CHAT_SCOPE = "https://www.googleapis.com/auth/chat.bot";
  static final String SPREADSHEET_SCOPE = "https://www.googleapis.com/auth/spreadsheets";

  ArrayList<String> SCOPES = new ArrayList<String>();

  private static final Logger logger = Logger.getLogger(Bot.class.getName());

  public static void main(String[] args) {
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

    // TODO: Initialize these into ArrayList
    SCOPES.add(CHAT_SCOPE);
    SCOPES.add(SPREADSHEET_SCOPE);

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
        String message = event.at("/message/text").asText();
        replyText = String.format("Your message: %s", message);
        break;
      case "REMOVED_FROM_SPACE":
        String name = event.at("/space/name").asText();
        logger.info(String.format("Bot removed from %s", name));
        break;
      default:
        replyText = "Cannot determine event type";
        break;
    }

    // [START async-response]

    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

    GoogleCredentials credentials =
        GoogleCredentials.fromStream(Bot.class.getResourceAsStream("/service-acct.json"))
            .createScoped(SCOPES);
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
    HangoutsChat chatService =
        new HangoutsChat.Builder(httpTransport, jsonFactory, requestInitializer)
            .setApplicationName("basic-async-bot-java")
            .build();

    // NEXT TASK:
    // Now that we have sheetsService working, create a spreadsheet in my own drive, save the ID
    // into a string
    // Then, find out how to have the bot request access to edit the sheet (probably going to take
    // the longest time to figure out)
    // Look into:
    // https://googleapis.dev/java/google-api-client/latest/com/google/api/client/googleapis/auth/oauth2/GoogleAuthorizationCodeFlow.html
    // And more importantly:
    // https://developers.google.com/api-client-library/java/google-api-java-client/oauth2
    // once that works, figure out how to write (append) to the sheet
    // After that, just build out the logic of reading and writing

    // TODO: see if I can use the same GoogleNetHTTPtransport

    // Create sheetsservice
    Sheets sheetsService =
        new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, requestInitializer)
            .setApplicationName("Google-SheetsSample/0.1")
            .build();

    // make a new sheet and tell the user the ID and URL
    Spreadsheet requestBody =
        new Spreadsheet().setProperties(new SpreadsheetProperties().setTitle("TEST 123"));
    Sheets.Spreadsheets.Create request = sheetsService.spreadsheets().create(requestBody);
    Spreadsheet response = request.execute();
    replyText = "ID: " + response.getSpreadsheetId() + "URL: " + response.getSpreadsheetUrl();

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

// [END async-bot]
