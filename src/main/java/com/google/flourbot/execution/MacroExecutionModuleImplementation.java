package com.google.flourbot.execution;

import com.google.flourbot.api.DriveClient;
import com.google.flourbot.api.CloudDocClient;
import com.google.flourbot.api.CloudSheet;
import com.google.flourbot.datastorage.DataStorage;
import com.google.flourbot.datastorage.FirebaseDataStorage;
import com.google.flourbot.entity.EntityModule;
import com.google.flourbot.entity.EntityModuleImplementation;
import com.google.flourbot.entity.Macro;
import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.action.ActionType;
import com.google.flourbot.entity.action.sheet.SheetAppendRowAction;
import com.google.flourbot.entity.action.sheet.SheetReadColumnAction;
import com.google.flourbot.entity.action.sheet.SheetReadRowAction;
import com.google.flourbot.entity.action.sheet.SheetReadSheetAction;
import com.google.flourbot.entity.action.sheet.SheetEntryType;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
* The logic class of the server that interfaces with the database and the sheet to execute
* the macro functionality and prepares contents of a message to be sent back the user
*/
public class MacroExecutionModuleImplementation implements MacroExecutionModule {

  private final EntityModule entityModule;
  private final CloudDocClient cloudDocClient;
  private static HashMap<String, String> threadMacroMap = new HashMap<String, String>();
  private static Map<String, Map<String, String>> roomToMacro = new HashMap<String, Map<String, String>>();

  /**
  * Returns a constructed MacroExecutionModuleImplementation as a singleton after
  * setting up Firebase server connection and connection to Google Sheets v4 api
  */
  public static MacroExecutionModuleImplementation initializeServer() {
    DataStorage dataStorage = new FirebaseDataStorage();
    EntityModule entityModule = new EntityModuleImplementation(dataStorage);
    CloudDocClient cloudDocClient = new DriveClient();
    return new MacroExecutionModuleImplementation(entityModule, cloudDocClient);
  }

  /**
  * Private constructor for MacroExecutionModuleImplementation to be called in initializeServer()
  */
  private MacroExecutionModuleImplementation(EntityModule entityModule, CloudDocClient cloudDocClient) {
    this.entityModule = entityModule;
    this.cloudDocClient = cloudDocClient;
  }

  /**
  * Splices a message to look for a keyword indicating what macro the user is trying to call
  * and saves it into a hashmap that stores the message thread that it originated in.
  * If the macroName is already stored for that thread, retrieve it.
  * @param message this is the text message the user typed in Hangouts Chat
  * @param threadId the identifier of the message thread in Hangouts Chat where the message came from
  * @return the macroName if found, an empty string if not found
  */
  public static String getMacroName(String message, String threadId) {
    
    // Retrieve macroname based on threadId
    if (threadMacroMap.containsKey(threadId)) {
      return threadMacroMap.get(threadId);
    }

    // If not yet stored, add threadId and macroName to hashmap
    String macroName = "";
    try {
      macroName = message.split(" ")[1];
    } catch(ArrayIndexOutOfBoundsException e) {
      throw new IllegalStateException(e);
    }

    threadMacroMap.put(threadId, macroName);
    return macroName;
  }

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
  public ChatResponse getReplyText(String message, String threadId, String roomId, String messageSenderEmail, String helpMessage) throws IOException, GeneralSecurityException{
        String macroName;
        String[] words = message.split(" ");
        Boolean isShareCommand = false;

        // Check that a message was written.
        if (words.length <= 1) {
            return new ChatResponse("You must type a message when you message me. Please type \"@MacroBot /help\" for more instructions.");
        }    

        // Reply to the help message.
        if (words[1].equalsIgnoreCase("/help")) {  
            return new ChatResponse(helpMessage);
        }

        // Get the macro name from the message. 
        if (words[1].equalsIgnoreCase("/share")) {
            String modifiedMessage = removeShareFromMessage(words);
            macroName = getMacroName(modifiedMessage, threadId);
            isShareCommand = true;
        }  
        else {
            macroName = getMacroName(message, threadId);
        }

        // If the macro has already been shared to this room, execute the macro. 
        if (roomToMacro.containsKey(roomId)) {
            Map<String, String> macroToCreator = roomToMacro.get(roomId);
            String macroCreatorEmail = macroToCreator.get(macroName);
            return execute(messageSenderEmail, macroCreatorEmail, message, threadId, macroName);
        }
        // If the macro has not been shared to the room, query the database for the macro.
        else {
            Optional<Macro> optionalMacro = entityModule.getMacro(messageSenderEmail, macroName);
            if (!optionalMacro.isPresent()) {
                return new ChatResponse(String.format("You do not own/have access to %s.", macroName));
            }
            else {
                // Share the macro if the creator writes "/share".
                if (isShareCommand == true) {
                    String macroCreatorEmail = messageSenderEmail;
                    Map<String, String> macroToCreator = new HashMap<String, String> ();
                    macroToCreator.put(macroName, macroCreatorEmail);

                    roomToMacro.put(roomId, macroToCreator);
                    return new ChatResponse(String.format("The %s macro belonging to %s has been shared to this room. All users in this room can use this macro.", macroName, macroCreatorEmail));
                }
                // Execute the macro if the creator just wants to use it instead of share it.
                else {
                    return execute(messageSenderEmail, messageSenderEmail, message, threadId, macroName);
                }
            }
        }
  }

  /**
  * Removes macro from being shared with room
  * @param roomId the room to stop sharing the macro in (roomId is retrieved from user message)
  */
  public void removeMacro(String roomId) {
      roomToMacro.remove(roomId);
  }

  /**
  * Handles the interpretation of the action retrieved from Firebase when the creators email
  * and macroname were queried and provides information for the response to be sent to user
  * @param userEmail the email of the user who messaged the macro
  * @param macroCreatorEmail the email of the user who created and owns the macro
  * @param threadId the id of the thread the user message originated in
  * @param macroName the name of the macro owned by creator
  *
  * @return a ChatResponse object that will be interpreted in Bot.java and CardResponse.java
  * to produce a text or card message to the user upon executing or failing to execute their
  * instructed action.
  */
  private ChatResponse execute(String userEmail, String macroCreatorEmail, String message, String threadId, String macroName) throws IOException, GeneralSecurityException {

    Optional<Macro> optionalMacro = entityModule.getMacro(macroCreatorEmail, macroName);
    if (!optionalMacro.isPresent()) {
      return new ChatResponse(String.format("No macro of name: %s found for %s", macroName, macroCreatorEmail));
    }

    Action action = optionalMacro.get().getAction();
    ActionType actionType = action.getActionType();
    String documentId = action.getDocumentId();
    String documentUrl = action.getDocumentUrl();
    CloudSheet cloudSheet = cloudDocClient.getCloudSheet(documentId);

    String replyText = "";
    ChatResponse chatResponse = null;

    switch (actionType) {
      case SHEET_APPEND_ROW:
        // Read instructions on what to write

        // If the message contains the macroName, remove "@Macrobot macroName" from message 
        if (message.contains(macroName)) {
            String[] messages = message.split(" ", 3);
            if (messages.length != 3) {
                throw new IllegalArgumentException();
            }
            message = messages[2];
        }
        // Otherwise if the message uses the threadId instead of explicitly stating the macroName, remove only "@Macrobot"
        else {
            String[] messages = message.split(" ", 2);
            if (messages.length != 2) {
                throw new IllegalArgumentException();
            }
            message = messages[1];
        }

        SheetEntryType[] columns = ((SheetAppendRowAction) action).getColumnValue();
        // Prepare values to write into the sheet
        ArrayList<String> values = new ArrayList<String>();

        for (SheetEntryType type : columns) {
          switch (type) {
            case TIME:
              values.add(getDate("dd-MM-yyyy HH:mm:ss"));
              break;

            case EMAIL:
              values.add(userEmail);
              break;

            case CONTENT:
              values.add(message);
              break;

            case EMPTY:
              values.add("");
              break;

            default:
              values.add("");
              break;

          }
        }

        // Append values to first free bottom row of sheet
        cloudSheet.appendRow(values);
        chatResponse = ChatResponse.createChatResponseWithList(values, actionType, documentUrl);
        break;

      // Read row of sheet and return ChatResponse containing a list of strings with cell contents
      case SHEET_READ_ROW:
        int row = ((SheetReadRowAction) action).getRow();
        List<String> rowData = cloudSheet.readRow(row);
        chatResponse = ChatResponse.createChatResponseWithList(rowData, actionType, documentUrl);
        break;

      // Read column of sheet and return ChatResponse containing a list of strings with cell contents
      case SHEET_READ_COLUMN:
        String column = ((SheetReadColumnAction) action).getColumn();
        List<String> columnData = cloudSheet.readColumn(column);
        chatResponse = ChatResponse.createChatResponseWithList(columnData, actionType, documentUrl);
        break;

      // Read sheet and return ChatResponse containing a list of strings with cell contents (inner list is each row)
      case SHEET_READ_SHEET:
        String sheetName = ((SheetReadSheetAction) action).getSheetName();
        List<List<String>> sheetData = cloudSheet.readSheet(sheetName);
        chatResponse = ChatResponse.createChatResponseWithListList(sheetData, actionType, documentUrl);
        break;
      
      default:
        throw new IllegalStateException(
            "Action type named: " + actionType.toString() + "is not implemented yet!");
    }

    return chatResponse;
  }

  /**
  * Create timestamp based on pattern provided
  * @param pattern with HH:mm:ss from Java date library
  * @return the timestamp as a string
  */
  private String getDate(String pattern) {
    LocalDateTime myDateObj = LocalDateTime.now();
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(pattern);
    return myDateObj.format(myFormatObj);
  }

  /**
  * Splices out the word "share" from the message
  */
  private String removeShareFromMessage (String[] words) {
      List<String> wordsWithoutShare = new LinkedList(Arrays.asList(words));
      wordsWithoutShare.remove("/share");
      return String.join(" ", wordsWithoutShare);
  }

  /**
  * Selects a random String from a list of Strings
  * @param data a list of strings to select a random string from
  * @return a string from that list
  */
  private String selectRandomFromData(List<String> data) {
    Random rand = new Random();
    return data.get(rand.nextInt(data.size()));
  }
}