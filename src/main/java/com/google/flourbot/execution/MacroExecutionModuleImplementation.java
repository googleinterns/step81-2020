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
import com.google.flourbot.entity.action.sheet.SheetAppendAction;
import com.google.flourbot.entity.action.sheet.SheetEntryType;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Optional;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

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
    if (this.threadMacroMap.containsKey(threadId)) {
      return this.threadMacroMap.get(threadId);
    }

    // If not yet stored, add threadId and macroName to hashmap
    String macroName = "";
    try {
      macroName = message.split(" ")[1];
    } catch(ArrayIndexOutOfBoundsException e) {
      throw new IllegalStateException(e);
    }

    this.threadMacroMap.put(threadId, macroName);
    return macroName;
  }


  public String execute(String userEmail, String message, String threadId) throws IOException, GeneralSecurityException {
    String macroName = this.getMacroName(message, threadId);

    Optional<Macro> optionalMacro = entityModule.getMacro(userEmail, macroName);
    if (!optionalMacro.isPresent()) {
      return "No macro of name: " + macroName + " found";
    }

    Action action = optionalMacro.get().getAction();
    ActionType actionType = action.getActionType();
    String documentId = action.getSheetId();
    CloudSheet cloudSheet = cloudDocClient.getCloudSheet(documentId);

    String replyText = "Action not recognized";

    switch (actionType) {
      case SHEET_APPEND:
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
            if (messages.length!= 2) {
                throw new IllegalArgumentException();
            }
            message = messages[1];
        }

        SheetEntryType[] columns = ((SheetAppendAction) action).getColumnValue();
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
        replyText = "Appended row to " + action.getSheetUrl();
        break;
      case SHEET_READ_ROW:
        List<String> rowData = cloudSheet.readRow(2); //TODO: Replace hardcoded
        replyText = rowData.get(0);
        break;
      case SHEET_READ_COLUMN:
        List<String> columnData = cloudSheet.readColumn("B");//TODO: Replace hardcoded
        replyText = columnData.get(0);
        break;
      default:
        throw new IllegalStateException(
            "Action type named: " + actionType.toString() + "is not implemented yet!");
    }

    // TODO: Return a response object
    return replyText;
  }

  private String getDate(String pattern) {
    // Create timestamp based on pattern provided
    LocalDateTime myDateObj = LocalDateTime.now();
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(pattern);
    return myDateObj.format(myFormatObj);
  }
}