package com.google.flourbot.executor;

import com.google.flourbot.datastorage.FirebaseDataStorage;
import com.google.flourbot.entity.Macro;
import com.google.flourbot.entity.trigger.Trigger;
import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.action.ActionType;
import com.google.flourbot.entity.EntityModule;

import java.util.Map;
import java.lang.Exception;
import java.util.Optional;
import com.google.cloud.firestore.QueryDocumentSnapshot;

// The Logic class of the server
public class Executor {

    private final EntityModule entityModule;
    //TODO: Add GoogleSheetHandler here when ready.

    public Executor(EntityModule entityModule) throws Exception {
        this.entityModule = EntityModule.getInstance();
    }

    public String execute(String userEmail, String message) throws IllegalStateException {
        
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
                //Document writer stuff

                break;
            default: 
                throw new IllegalStateException("Action type named: " + actionType.toString() + "is not implemented yet!" );
        }

        // TODO: Return a response object 
        return "Sucessfully executed";        
    }

} 