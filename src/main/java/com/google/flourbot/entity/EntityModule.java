package com.google.flourbot.entity;

import com.google.flourbot.entity.EntityInterface;
import com.google.flourbot.datastorage.FirebaseDataStorage;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.action.SheetAppendAction;
import com.google.flourbot.entity.trigger.Trigger;
import com.google.flourbot.entity.trigger.CommandTrigger;
import java.util.Map;

import java.lang.Exception;

public class EntityModule implements EntityInterface {
    
    private static EntityModule instance = null;
    private final FirebaseDataStorage datastorage;

    private EntityModule() {
        this.datastorage = new FirebaseDataStorage();
    }

    public Macro getMacro(String userEmail, String macroName) throws Exception {
        QueryDocumentSnapshot document = this.datastorage.getDocument(userEmail, macroName); 
        //TODO 
        //Handle Java "Optional<QueryDocumentSnapshot>" once getDocument function is modified" 

        Map<String, Object> macroMap = document.getData();
        Map<String, Object> triggerMap = (Map<String, Object>) macroMap.get("trigger");
        Map<String, Object> actionMap = (Map<String, Object>) macroMap.get("action");

        String creatorId = (String) macroMap.get("creatorId");
        Trigger trigger = getTrigger(triggerMap);
        Action action = getAction(actionMap);

        Macro macro = new Macro(creatorId, macroName, trigger, action);

        return macro;
    }

    private Trigger getTrigger(Map<String, Object> triggerMap) throws Exception {

        Trigger trigger;        
        String triggerType = (String) triggerMap.get("type");

        switch(triggerType) {
            case ("Command Trigger"): 
                String command = (String) triggerMap.get("command");
                trigger = new CommandTrigger(command);
                break;
            default:
                throw new Exception("No Trigger Type found");
        }

        return trigger;
    }

    private Action getAction(Map<String, Object> actionMap) throws Exception {
        
        Action action;
        String actionType = (String) actionMap.get("type");

        switch(actionType) {
            case ("Sheet Action"):
                String[] columnValue = (String[]) actionMap.get("columnValue");
                String sheetAction = (String) actionMap.get("sheetAction");
                String sheetUrl = (String) actionMap.get("sheetUrl");

                action = new SheetAppendAction(columnValue, sheetAction, sheetUrl);
                break;
            default:
                throw new Exception("No Action Type found");
        }

        return action;
    }


    // Singleton Support
    public static EntityModule getInstance() {
        if (instance == null) {
            instance = new EntityModule();
        }
        return instance;
    }
}