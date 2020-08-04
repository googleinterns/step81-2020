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

// The Logic class of the server
public class MacroExecutionModuleImplementation implements MacroExecutionModule {

  private final EntityModule entityModule;
  private final CloudDocClient cloudDocClient;
  private static Map<String, String> threadMacroMap = new HashMap<String, String>();
  private static Map<String, Map<String, String>> roomToMacro = new HashMap<String, Map<String, String>>();

  public static MacroExecutionModuleImplementation initializeServer() {
    DataStorage dataStorage = new FirebaseDataStorage();
    EntityModule entityModule = new EntityModuleImplementation(dataStorage);
    CloudDocClient cloudDocClient = new DriveClient();
    return initializeServer(entityModule, cloudDocClient);
  }

  public static MacroExecutionModuleImplementation initializeServer(EntityModule entityModule, CloudDocClient cloudDocClient) {
    return new MacroExecutionModuleImplementation(entityModule, cloudDocClient);
  }

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

  public void removeMacro(String roomId) {
      roomToMacro.remove(roomId);
  }

  // Used for testing
  public Map<String, Map<String, String>> getRoomToMacro() {
      return this.roomToMacro;
  }
  public Map<String, String> getThreadMacroMap() {
      return this.threadMacroMap;
  }

  private MacroExecutionModuleImplementation(EntityModule entityModule, CloudDocClient cloudDocClient) {
    this.entityModule = entityModule;
    this.cloudDocClient = cloudDocClient;
  }

  private ChatResponse execute(String userEmail, String macroCreatorEmail, String message, String threadId, String macroName) throws IOException, GeneralSecurityException {
    
    Optional<Macro> optionalMacro = entityModule.getMacro(userEmail, macroName);
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

      case SHEET_READ_ROW:
        int row = ((SheetReadRowAction) action).getRow();
        List<String> rowData = cloudSheet.readRow(row);
        chatResponse = ChatResponse.createChatResponseWithList(rowData, actionType, documentUrl);
        break;

      case SHEET_READ_COLUMN:
        String column = ((SheetReadColumnAction) action).getColumn();
        List<String> columnData = cloudSheet.readColumn(column);
        chatResponse = ChatResponse.createChatResponseWithList(columnData, actionType, documentUrl);
        break;

      case SHEET_READ_SHEET:
        String sheetName = ((SheetReadSheetAction) action).getSheetName();
        List<List<String>> sheetData = cloudSheet.readSheet(sheetName);
        chatResponse = ChatResponse.createChatResponseWithListList(sheetData, actionType, documentUrl);
        break;
      
      // TODO: Add random option that uses function selectRandomFromData(data);
      default:
        throw new IllegalStateException(
            "Action type named: " + actionType.toString() + "is not implemented yet!");
    }

    return chatResponse;
  }

  private String getDate(String pattern) {
    // Create timestamp based on pattern provided
    LocalDateTime myDateObj = LocalDateTime.now();
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(pattern);
    return myDateObj.format(myFormatObj);
  }

  private String removeShareFromMessage (String[] words) {
      List<String> wordsWithoutShare = new LinkedList(Arrays.asList(words));
      wordsWithoutShare.remove("/share");
      return String.join(" ", wordsWithoutShare);
  }

  private String selectRandomFromData(List<String> data) {
    Random rand = new Random();
    return data.get(rand.nextInt(data.size()));
  }
}