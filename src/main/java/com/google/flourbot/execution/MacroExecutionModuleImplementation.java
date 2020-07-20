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

// The Logic class of the server
public class MacroExecutionModuleImplementation implements MacroExecutionModule {

  private MacroExecutionModuleImplementation instance = null;
  private final EntityModule entityModule;

  private MacroExecutionModuleImplementation(EntityModule entityModule) {
    this.entityModule = entityModule;
  }

  public static MacroExecutionModuleImplementation getMacroExecutionModule() {
    if (instance == null) {
      DataStorage dataStorage = new FirebaseDataStorage();
      EntityModule entityModule = new EntityModuleImplementation(dataStorage);

      return new MacroExecutionModuleImplementation(entityModule);
    } else {
      return instance;
    }
  }

  public String execute(String userEmail, String message) throws IOException, GeneralSecurityException {

    String macroName = message.split(" ")[1];

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
        DriveClient cdc = new DriveClient();
        CloudSheet cs = cdc.getCloudSheet(documentId);
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
