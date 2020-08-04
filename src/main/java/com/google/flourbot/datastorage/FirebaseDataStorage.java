package com.google.flourbot.datastorage;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FirebaseDataStorage implements DataStorage {
  private Firestore db;
  private static final String PROJECT_ID = "stepladder-2020";
  private static final String SERVICE_ACCOUNT = "/key.json";
  private static final String COLLECTION_NAME = "macros";
  private static final String CREATOR_IDENTIFIER = "creatorId";
  private static final String MACRO_IDENTIFIER = "macroName";

  public FirebaseDataStorage() {
    try {
      GoogleCredentials credentials = GoogleCredentials.fromStream(
            FirebaseDataStorage.class.getResourceAsStream(SERVICE_ACCOUNT)
      );

      FirebaseOptions options = new FirebaseOptions.Builder()
            .setProjectId(PROJECT_ID)
            .setCredentials(credentials)
            .build();

      FirebaseApp.initializeApp(options);
      this.db =  FirestoreClient.getFirestore();

    } catch (FileNotFoundException e) {
      throw new IllegalStateException(e);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
  public Optional<Map<String, Object>> getDocument(String userEmail, String macroName) {
    // Create a query to find a macro named macroName belonging to userEmail
    Query query =
        db.collection(COLLECTION_NAME)
            .whereEqualTo(MACRO_IDENTIFIER, macroName)
            .whereEqualTo(CREATOR_IDENTIFIER, userEmail);
    // Retrieve  query results asynchronously using query.get()
    ApiFuture<QuerySnapshot> querySnapshot = query.get();

    // Get the document representing the queried macro in firebase
    Map<String, Object> data = null;
    
    try {
      // Get the document representing the queried macro in firebase
      data = querySnapshot.get().getDocuments().get(0).getData();
      return data == null ? Optional.empty() :  Optional.of(data);
    } catch (IndexOutOfBoundsException e) {
      return Optional.empty();
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    } catch (ExecutionException e) {
      throw new IllegalStateException(e);
    }
  }

}