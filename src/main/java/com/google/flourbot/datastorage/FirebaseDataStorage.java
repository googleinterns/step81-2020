package com.google.flourbot.datastorage;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;

import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FirebaseDataStorage implements DataStorage {
  private Firestore db;
  private static final String PROJECT_ID = "stepladder-2020";
  private static final String SERVICE_ACCOUNT = "/key.json";

  public FirebaseDataStorage() {
    try {
      this.db = initializeFirebase();
    } catch (FileNotFoundException e) {
      throw new IllegalStateException(e);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public Optional<QueryDocumentSnapshot> getDocument(String userEmail, String macroName) {

    // Create a query to find a macro named macroName belonging to userEmail
    Query query =
        db.collection("macros")
            .whereEqualTo("creatorId", userEmail)
            .whereEqualTo("macroName", macroName);
    // Retrieve  query results asynchronously using query.get()
    ApiFuture<QuerySnapshot> querySnapshot = query.get();

    try {
      // Get the document representing the queried macro in firebase
      QueryDocumentSnapshot document = querySnapshot.get().getDocuments().get(0);
      if (document.exists()) {
        return Optional.of(document);
      } else {
        return Optional.empty();
      }
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    } catch (ExecutionException e) {
      throw new IllegalStateException(e);
    }
  }

  private Firestore initializeFirebase() throws IOException {

    GoogleCredentials credentials = GoogleCredentials.fromStream(
            FirebaseDataStorage.class.getResourceAsStream(SERVICE_ACCOUNT)
    );

    FirebaseOptions options = new FirebaseOptions.Builder()
            .setProjectId(PROJECT_ID)
            .setCredentials(credentials)
            .build();

    FirebaseApp.initializeApp(options);
    Firestore dbFirestore = FirestoreClient.getFirestore();
    return dbFirestore;
  }
}
