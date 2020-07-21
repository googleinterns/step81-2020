package com.google.flourbot.entity;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.flourbot.datastorage.DataStorage;
import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.action.SheetAppendAction;
import com.google.flourbot.entity.trigger.CommandTrigger;
import com.google.flourbot.entity.trigger.Trigger;

import java.util.Map;
import java.util.Optional;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;

public class EntityModuleImplementation implements EntityModule {

  private final DataStorage datastorage;

  public EntityModuleImplementation(DataStorage dataStorage) {
    this.datastorage = dataStorage;
  }

  public Optional<Macro> getMacro(String userEmail, String macroName) {
    Optional<QueryDocumentSnapshot> optionalDocument =
        datastorage.getDocument(userEmail, macroName);

    if (!optionalDocument.isPresent()) {
      // Handle the case if an empty document is returned.
      return Optional.empty();
    }

    // Retrieve value from optional if it's not empty
    QueryDocumentSnapshot document = optionalDocument.get();

    Map<String, Object> macroMap = document.getData();

    Map<String, Object> triggerMap = (Map<String, Object>) macroMap.get("trigger");
    Map<String, Object> actionMap = (Map<String, Object>) macroMap.get("action");

    Optional<Trigger> optionalTrigger = getTrigger(triggerMap);
    Optional<Action> optionalAction = getAction(actionMap);

    if (!optionalTrigger.isPresent() || !optionalAction.isPresent()) {
      return Optional.empty();
    }

    Trigger trigger = optionalTrigger.get();
    Action action = optionalAction.get();

    String creatorId = (String) macroMap.get("creatorId");
    Macro macro = new Macro(creatorId, macroName, trigger, action);

    return Optional.of(macro);
  }

  private Optional<Trigger> getTrigger(Map<String, Object> triggerMap) {

    String triggerType = (String) triggerMap.get("type");

    switch (triggerType) {
      case ("Command Trigger"):
        String command = (String) triggerMap.get("command");
        Trigger trigger = new CommandTrigger(command);

        return Optional.of(trigger);

      default:
        return Optional.empty();
    }
  }

  private Optional<Action> getAction(Map<String, Object> actionMap) {

    String actionType = (String) actionMap.get("type");

    switch (actionType) {
      case ("Sheet Action"):
        ArrayList<String> colValList = (ArrayList<String>) actionMap.get("columnValue");
        String[] columnValue = colValList.stream().toArray(String[]::new);
        String sheetAction = (String) actionMap.get("sheetAction");
        String sheetUrl = (String) actionMap.get("sheetUrl");
        Action action = new SheetAppendAction(columnValue, sheetAction, sheetUrl);
        return Optional.of(action);
      default:
        return Optional.empty();
    }
  }

}
