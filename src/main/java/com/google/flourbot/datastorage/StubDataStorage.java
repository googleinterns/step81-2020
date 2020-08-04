package com.google.flourbot.datastorage;

import java.util.Map;
import java.util.Optional;

public class StubDataStorage implements DataStorage{
  private Map<String, Object> map;

  public Optional<Map<String, Object>> getDocument(String userEmail, String macroName) {
    return Optional.ofNullable(this.map);
  }

  public void setMap(Map<String, Object> map) {
    this.map = map;
  }
}
