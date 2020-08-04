package com.google.flourbot.datastorage;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.util.Optional;

public class StubDataStorage implements DataStorage{
  private QueryDocumentSnapshot queryDocumentSnapshot;

  @Override
  public Optional<QueryDocumentSnapshot> getDocument(String userEmail, String macroName) {
    return Optional.ofNullable(this.queryDocumentSnapshot);
  }
}
