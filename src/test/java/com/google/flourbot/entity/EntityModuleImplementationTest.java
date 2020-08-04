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
import java.util.Optional;

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

  // Test getMacro under normal scenario - Database returns a well formed Macro Map object
  @Test
  void getMacro() throws IOException {
    String mockJson = loadTestJson("mock_macro.json");
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
    Macro actualMacro = entityModuleImplementation.getMacro("tonyshen@google.com", "Daily").get();

    assertEquals(actualMacro, expectedMacro);
  }

  // Test scenario where Map received is null.
  @Test
  void getMacroEmpty() {
    dataStorage.setMap(null);
    Optional<Macro> optionalMacro = entityModuleImplementation.getMacro("", "");
    assertTrue(optionalMacro.isEmpty());
  }

  @Test
  void getMacroEmptyAction() throws IOException {
    String mockJson = loadTestJson("mock_macro_no_action.json");
    Map<String, Object> mockMap =
        new ObjectMapper().readValue(mockJson, Map.class);
    dataStorage.setMap(mockMap);

    Optional<Macro> optionalMacro = entityModuleImplementation.getMacro("tonyshen@google.com", "Daily");
    assertTrue(optionalMacro.isEmpty());
  }

  @Test
  void getMacroEmptyTrigger() throws IOException {
    String mockJson = loadTestJson("mock_macro_no_trigger.json");
    Map<String, Object> mockMap =
        new ObjectMapper().readValue(mockJson, Map.class);
    dataStorage.setMap(mockMap);

    Optional<Macro> optionalMacro = entityModuleImplementation.getMacro("tonyshen@google.com", "Daily");
    assertTrue(optionalMacro.isEmpty());
  }

  @org.junit.jupiter.api.AfterEach
  void tearDown() {}
}