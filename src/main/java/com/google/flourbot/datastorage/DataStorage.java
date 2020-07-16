package com.google.flourbot.datastorage;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;
import java.util.Optional;

public interface DataStorage {
    Optional<QueryDocumentSnapshot> getDocument (String userEmail, String macroName) throws InterruptedException, ExecutionException; 
}