package com.google.flourbot.datastorage;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

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
  private static final String COLLECTION_NAME = "macros";
  private static final String USER_IDENTIFIER = "creatorId";
  private static final String MACRO_IDENTIFIER = "macroName";

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
        db.collection(COLLECTION_NAME)
            .whereEqualTo(USER_IDENTIFIER, userEmail)
            .whereEqualTo(MACRO_IDENTIFIER, macroName);
    // Retrieve  query results asynchronously using query.get()
    ApiFuture<QuerySnapshot> querySnapshot = query.get();

    try {
      // Get the document representing the queried macro in firebase
      QueryDocumentSnapshot document = querySnapshot.get().getDocuments().get(0);
      return document.exists() ? Optional.of(document) : Optional.empty();
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
    return FirestoreClient.getFirestore();
  }
}
