package com.google.flourbot.executor;

import com.google.flourbot.datastorage.FirebaseDataStorage;
import com.google.flourbot.entity.Macro;
import com.google.flourbot.entity.trigger.Trigger;
import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.EntityModule;

import java.util.Map;
import java.lang.Exception;
import com.google.cloud.firestore.QueryDocumentSnapshot;

// The Logic class of the server
public class Executor {

    private final EntityModule entityModule;
    //TODO: Add GoogleSheetHandler here when ready.

    public Executor(EntityModule entityModule) throws Exception {
        this.entityModule = EntityModule.getInstance();
    }

    public String execute(String userEmail, String message) throws Exception {
        
        String macroName = message.split(" ")[0];

        Macro macro = entityModule.getMacro(userEmail, macroName);
        Action action = macro.getAction();

        switch (action.getActionType()) {
            case SHEET_APPEND:
                //Document writer stuff

                break;
            default: 
                throw new Exception("Unknown action type");
        }

        return "Sucessfully executed";        
    }

} 