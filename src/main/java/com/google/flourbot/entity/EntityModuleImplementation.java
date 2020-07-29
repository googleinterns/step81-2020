package com.google.flourbot.entity;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.flourbot.datastorage.DataStorage;
import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.action.sheet.SheetAppendRowAction;
import com.google.flourbot.entity.action.sheet.SheetReadColumnAction;
import com.google.flourbot.entity.action.sheet.SheetReadRowAction;
import com.google.flourbot.entity.action.sheet.SheetReadSheetAction;
import com.google.flourbot.entity.action.sheet.SheetEntryType;
import com.google.flourbot.entity.trigger.CommandTrigger;
import com.google.flourbot.entity.trigger.Trigger;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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

    Map<String, Object> macroData = document.getData();

    Map<String, Object> triggerData = (Map<String, Object>) macroData.get("trigger");
    Map<String, Object> actionData = (Map<String, Object>) macroData.get("action");

    Optional<Trigger> optionalTrigger = getTrigger(triggerData);
    Optional<Action> optionalAction = getAction(actionData);

    if (!optionalTrigger.isPresent() || !optionalAction.isPresent()) {
      return Optional.empty();
    }

    Trigger trigger = optionalTrigger.get();
    Action action = optionalAction.get();

    String creatorId = (String) macroData.get("creatorId");
    Macro macro = new Macro(creatorId, macroName, trigger, action);

    return Optional.of(macro);
  }

  private Optional<Trigger> getTrigger(Map<String, Object> triggerData) {

    String triggerType = (String) triggerData.get("type");

    switch (triggerType) {
      case ("Command Trigger"):
        String command = (String) triggerData.get("command");
        Trigger trigger = new CommandTrigger(command);

        return Optional.of(trigger);

      default:
        return Optional.empty();
    }
  }

  private Optional<Action> getAction(Map<String, Object> actionData) {

    String actionType = (String) actionData.get("type");

    switch (actionType) {
      case ("Sheet Action"):
        ArrayList<String> columnStringList = (ArrayList<String>) actionData.get("columnValue");
        // Converts into ENUM type
        ArrayList<SheetEntryType> columnTypeList = new ArrayList<>();
        for (String type : columnStringList) {
            columnTypeList.add(SheetEntryType.valueOf(type));
        }
        SheetEntryType[] columnValue = columnTypeList.stream().toArray(SheetEntryType[]::new);

        String sheetAction = (String) actionData.get("sheetAction");
        String sheetUrl = (String) actionData.get("sheetUrl");

        Action action = null;

        switch(sheetAction) {
          case ("Sheet Append Action"):
            action = new SheetAppendRowAction(sheetUrl, columnValue);
            break;
 
          case ("Read Row Action"):
            Object row = actionData.get("row");
            if (row == null) {
              throw new IllegalStateException("row not found in Firestore action");
            }
            action = new SheetReadRowAction(sheetUrl, (int) row);
            break;

          case ("Read Column Action"):
            // TODO: also handle column as an integer, or just completely remove that logic
            Object column = actionData.get("column");
            if (column == null) {
              throw new IllegalStateException("column not found in Firestore action");
            }
            action = new SheetReadColumnAction(sheetUrl, (String) column);
            break;

          case ("Read Sheet Action"):
            Object sheetName = actionData.get("sheetName");
            if (sheetName == null) {
              throw new IllegalStateException("sheetName not found in Firestore action");
            }
            action = new SheetReadSheetAction(sheetUrl, (String) sheetName);
            break;

          case ("Batch Action"):
            break;

          default:
            throw new IllegalStateException("Action type not recognized");
        }

        return Optional.of(action);
      default:
        return Optional.empty();
    }
  }

}
