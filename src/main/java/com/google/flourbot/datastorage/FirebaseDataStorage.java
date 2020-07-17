package com.google.flourbot.datastorage;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FirebaseDataStorage implements DataStorage {
  private Firestore db;
  private static final String projectId = "stepladder-2020";
  private static final String serviceAccountFilePath = "key.json";

  public FirebaseDataStorage() {
    try {
      this.db = initializeFirebase();
    } catch (FileNotFoundException e) {
      new IllegalStateException(e);
    } catch (IOException e) {
      new IllegalStateException(e);
    }
  }

  public Optional<QueryDocumentSnapshot> getDocument(String userEmail, String macroName)
      throws InterruptedException, ExecutionException {
    // Create a query to find a macro named macroName belonging to userEmail
    Query query =
        db.collection("macros")
            .whereEqualTo("creatorId", userEmail)
            .whereEqualTo("macroName", macroName);
    // Retrieve  query results asynchronously using query.get()
    ApiFuture<QuerySnapshot> querySnapshot = query.get();

    // Get the document representing the queried macro in firebase
    QueryDocumentSnapshot document = querySnapshot.get().getDocuments().get(0);

    // Use Optional.Empty() to indicate no macro is found.
    if (document.exists()) {
      return Optional.of(document);
    } else {
      return Optional.empty();
    }
  }

  private Firestore initializeFirebase() throws IOException {
    FileInputStream serviceAccount;
    serviceAccount = new FileInputStream(serviceAccountFilePath);
    FirestoreOptions firestoreOptions =
        FirestoreOptions.getDefaultInstance().toBuilder()
            .setProjectId(projectId)
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();
    Firestore db = firestoreOptions.getService();
    return db;
  }
}
