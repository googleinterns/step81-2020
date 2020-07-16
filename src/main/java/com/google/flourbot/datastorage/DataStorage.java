package com.google.flourbot.datastorage;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.util.Optional;

public interface DataStorage {
    Optional<QueryDocumentSnapshot> getDocument (String userEmail, String macroName); 
}