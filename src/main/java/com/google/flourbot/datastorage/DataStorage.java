package com.google.flourbot.datastorage;

import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface DataStorage {
  Optional<QueryDocumentSnapshot> getDocument(String userEmail, String macroName)
      throws InterruptedException, ExecutionException;
}
