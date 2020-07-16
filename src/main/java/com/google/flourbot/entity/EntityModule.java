package com.google.flourbot.entity;

import com.google.flourbot.entity.EntityInterface;
import com.google.flourbot.datastorage.FirebaseDataStorage;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.util.Map;

public class EntityModule implements EntityInterface {
    
    private static EntityModule instance = null;
    private final FirebaseDataStorage datastorage;

    private EntityModule() {
        this.datastorage = new FirebaseDataStorage();
    }

    public Macro getMacro(String userEmail, String macroName) {
        QueryDocumentSnapshot document = dataStorage.getDocument(userEmail, macroName); 
        //TODO 
        //Handle Java "Optional<QueryDocumentSnapshot>" once getDocument function is modified" 

        Map<String, Object> map = document.getData();
        Macro macro = new Macro(map);

        return macro;
    }

    // Singleton Support
    public static EntityModule getInstance() {
        if (instance == null) {
            instance = new EntityModule();
        }
        return instance;
    }
}