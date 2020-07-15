package com.google.flourbot.entity;

public class EntityModule extends EntityInterface {
    
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