package com.google.flourbot.executor;

import com.google.flourbot.datastorage.FirebaseDataStorage;
import com.google.flourbot.entity.Macro;
import com.google.flourbot.entity.Trigger;
import com.google.flourbot.entity.Action;

import java.util.Map;
import java.lang.Exception;
import com.google.cloud.firestore.QueryDocumentSnapshot;

// The Logic class of the server
public class Executor {

    private final String userEmail;
    private final FirebaseDataStorage dataStorage;
    
    public Executor(String userEmail, FirebaseDataStorage dataStorage) throws Exception {
        this.userEmail = userEmail;
        this.dataStorage = dataStorage;
    }


    public String execute(String payload) throws Exception {
        
        String macroName = payload.split(" ")[0];
        QueryDocumentSnapshot document = dataStorage.getDocument(userEmail, macroName); 

        if (document == null) {
            throw new Exception("Bot is not found");      
        } 
        
        Map<String, Object> map = document.getData();
        Macro macro = new Macro(map);
        
        String actionType = macro.getAction().getSheetAction();
        switch (actionType) {
            case ("Sheet Action"):
                //Document writer stuff

                break;
            default: 
                throw new Exception("Unknown action type");
        }

        return "Sucessfully executed";        
    }

} 