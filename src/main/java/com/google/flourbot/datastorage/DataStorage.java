package com.google.flourbot.datastorage;

import com.google.cloud.firestore.QueryDocumentSnapshot;

public interface DataStorage {
    QueryDocumentSnapshot getDocument (String userEmail, String macroName) throws Exception; 
}