/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.firebaseserver.app;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.common.collect.ImmutableMap;

import java.io.*;

public class FirebaseDataStorage implements DataStorage {
    private Firestore db;

    public FirebaseDataStorage () {
        try {
            this.db = initializeFirebase();
        }
        catch (FileNotFoundException e) {
            new IllegalStateException(e); 
        }
        catch (IOException e) {
            new IllegalStateException(e);
        }
    }
 


    public QueryDocumentSnapshot getDocument (String userEmail, String message) throws Exception {
        String macroName = getMacroName(message);
        
        // Create a query to find a macro named macroName belonging to userEmail
        Query query = db.collection("macros").whereEqualTo("creatorId", userEmail).whereEqualTo("macroName", macroName);
        // Retrieve  query results asynchronously using query.get()
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        // Get the document representing the queried macro in firebase
        QueryDocumentSnapshot document = querySnapshot.get().getDocuments().get(0);
        return document;
    }

    private Firestore initializeFirebase () throws FileNotFoundException, IOException {
        FileInputStream serviceAccount;     
        serviceAccount = new FileInputStream("key.json");
            FirestoreOptions firestoreOptions =
            FirestoreOptions.getDefaultInstance().toBuilder()
                .setProjectId("stepladder-2020")
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
            Firestore db = firestoreOptions.getService();
            return db;
    }

    private String getMacroName (String message) {
        String[] words = message.split(" ");
        return words[0];
    }

}