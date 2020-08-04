package com.google.flourbot.datastorage;

import java.util.Map;
import java.util.Optional;

public interface DataStorage {
  Optional<Map<String, Object>> getDocument(String userEmail, String macroName);
}
