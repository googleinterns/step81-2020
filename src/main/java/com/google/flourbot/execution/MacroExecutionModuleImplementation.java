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

import java.util.Optional;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// The Logic class of the server
public class MacroExecutionModuleImplementation implements MacroExecutionModule {

  private final EntityModule entityModule;
  private final CloudDocClient cloudDocClient;
  private HashMap<String, String> threadMacroMap = new HashMap<String, String>();

  private MacroExecutionModuleImplementation(EntityModule entityModule, CloudDocClient cloudDocClient) {
    this.entityModule = entityModule;
    this.cloudDocClient = cloudDocClient;
  }

  public static MacroExecutionModuleImplementation initializeServer() {
    DataStorage dataStorage = new FirebaseDataStorage();
    EntityModule entityModule = new EntityModuleImplementation(dataStorage);
    CloudDocClient cloudDocClient = new DriveClient();
    return new MacroExecutionModuleImplementation(entityModule, cloudDocClient);
  }

  private String getMacroName(String message, String threadId) {
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

  public ChatResponse execute(String userEmail, String message, String threadId) throws IOException, GeneralSecurityException {
    
    //Check if the message is a help message
    String[] words = message.split(" ", 2);
    if (words[1].equalsIgnoreCase("help")) {
        return new ChatResponse("To use your macro, please type \"@MacroBot MacroName <your message>\". If your macro has already been used in a room's thread, you may omit the MacroName and can simply write \"@MacroBot <your message>\". ");
    }

    String macroName = getMacroName(message, threadId);
    
    Optional<Macro> optionalMacro = entityModule.getMacro(userEmail, macroName);
    if (!optionalMacro.isPresent()) {
      return new ChatResponse(String.format("No macro of name: %s found", macroName));
    }

    Action action = optionalMacro.get().getAction();
    ActionType actionType = action.getActionType();
    String documentId = action.getSheetId();
    String documentUrl = action.getSheetUrl();
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

  private String selectRandomFromData(List<String> data) {
    Random rand = new Random();
    return data.get(rand.nextInt(data.size()));
  }
}