package com.google.flourbot.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Charsets;
import com.google.appengine.repackaged.com.google.common.io.Resources;
import com.google.flourbot.datastorage.StubDataStorage;
import com.google.flourbot.entity.action.sheet.SheetAppendRowAction;
import com.google.flourbot.entity.action.sheet.SheetEntryType;
import com.google.flourbot.entity.trigger.CommandTrigger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EntityModuleImplementationTest {

  private StubDataStorage dataStorage;
  private EntityModuleImplementation entityModuleImplementation;

  public String loadTestJson(String fileName) {
    URL url = Resources.getResource(fileName);
    try {
      String data = Resources.toString(url, Charsets.UTF_8);
      return data;
    } catch (IOException e) {
      throw new RuntimeException("Couldn't load a JSON file.", e);
    }
  }

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    dataStorage = new StubDataStorage();
    entityModuleImplementation  = new EntityModuleImplementation(dataStorage);
  }

  @Test
  void getMacro() throws IOException {
    String mockJson = loadTestJson("mock_macro");
    Map<String, Object> mockMap =
        new ObjectMapper().readValue(mockJson, Map.class);
    dataStorage.setMap(mockMap);

    Macro expectedMacro = new Macro(
        "tonyshen@google.com",
        "Daily",
        new CommandTrigger("123"),
        new SheetAppendRowAction(
            "https://docs.google.com/spreadsheets/d/YOUR_SHEET_ID_HERE/edit#gid=0", new SheetEntryType[0])
    );

    Macro actualMacro = entityModuleImplementation.getMacro("", "").get();

    assertEquals(expectedMacro, actualMacro);
  }

  @org.junit.jupiter.api.AfterEach
  void tearDown() {}
}