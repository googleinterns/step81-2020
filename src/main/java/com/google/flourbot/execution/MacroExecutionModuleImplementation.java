package com.google.flourbot.execution;

import com.google.flourbot.datastorage.DataStorage;
import com.google.flourbot.datastorage.FirebaseDataStorage;
import com.google.flourbot.entity.EntityModule;
import com.google.flourbot.entity.EntityModuleImplementation;
import com.google.flourbot.entity.Macro;
import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.action.ActionType;

import javax.crypto.Mac;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

// The Logic class of the server
public class MacroExecutionModuleImplementation {

  private final EntityModule entityModule;

  private MacroExecutionModuleImplementation(EntityModule entityModule) {
    this.entityModule = entityModule;
  }

  public static MacroExecutionModuleImplementation initalizeServer() {
    DataStorage dataStorage = new FirebaseDataStorage();
    EntityModule entityModule = new EntityModuleImplementation(dataStorage);

    return new MacroExecutionModuleImplementation(entityModule);
  }

  public String execute(String userEmail, String message)
      throws IllegalStateException, InterruptedException, ExecutionException {

    String macroName = message.split(" ")[0];

    Optional<Macro> optionalMacro = entityModule.getMacro(userEmail, macroName);
    if (!optionalMacro.isPresent()) {
      throw new IllegalStateException("No macro named: " + macroName + " found!");
    }

    Macro macro = optionalMacro.get();
    Action action = macro.getAction();
    ActionType actionType = action.getActionType();

    switch (actionType) {
      case SHEET_APPEND:
        // Document writer stuff

        break;
      default:
        throw new IllegalStateException(
            "Action type named: " + actionType.toString() + "is not implemented yet!");
    }

    // TODO: Return a response object
    return "Sucessfully executed";
  }
}
