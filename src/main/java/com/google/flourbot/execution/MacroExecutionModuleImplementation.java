package com.google.flourbot.execution;

import com.google.flourbot.api.CloudDocClient;
import com.google.flourbot.api.DriveClient;
import com.google.flourbot.api.CloudSheet;
import com.google.flourbot.api.DriveCloudSheet;
import com.google.flourbot.datastorage.DataStorage;
import com.google.flourbot.datastorage.FirebaseDataStorage;
import com.google.flourbot.entity.EntityModule;
import com.google.flourbot.entity.EntityModuleImplementation;
import com.google.flourbot.entity.Macro;
import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.action.ActionType;
import com.google.flourbot.entity.action.SheetAppendAction;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.Optional;
import java.util.HashMap;

// The Logic class of the server
public class MacroExecutionModuleImplementation implements MacroExecutionModule {

  private final EntityModule entityModule;
  private final DriveClient cloudDocClient;
  private HashMap<String, String> threadMacroMap = new HashMap<String, String>();

  private MacroExecutionModuleImplementation(EntityModule entityModule, DriveClient cloudDocClient) {
    this.entityModule = entityModule;
    this.cloudDocClient = cloudDocClient;
  }

  public static MacroExecutionModuleImplementation initializeServer() {
    DataStorage dataStorage = new FirebaseDataStorage();
    EntityModule entityModule = new EntityModuleImplementation(dataStorage);
    DriveClient cloudDocClient = new DriveClient();
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

    switch (actionType) {
      case SHEET_APPEND:
        // Create timestamp
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);

        // Prepare values to write into the sheet
        ArrayList<String> values = new ArrayList<String>();
        values.add(formattedDate);
        values.add(userEmail);
        values.add(message);

        // Append values to first free bottom row of sheet
        SheetAppendAction a = (SheetAppendAction) optionalMacro.get().getAction();
        String documentId = a.getSheetId();
        CloudSheet cs = this.cloudDocClient.getCloudSheet(documentId);
        cs.appendRow(values);
        break;
      default:
        throw new IllegalStateException(
            "Action type named: " + actionType.toString() + "is not implemented yet!");
    }

    // TODO: Return a response object
    return "Sucessfully executed";
  }
}
