package com.google.flourbot.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public final class DriveCloudSheetTest {

  // Create a sheet where in every cell A1:E5, the cell contents is the location
  // Ex: The top left cell at A1 will contain the string "A1"
  // Remember to give the bot permission to edit the sheet
  private static final String SPREADSHEET_ID = "1ruk-1_izE0iiOWqf5mgyHkOcW2dCNI09uy28kuceJtQ";
  private static final String SHEET_NAME = "Sheet1";

  private static final String RANGE_SINGLE = "B1";
  private static final String RANGE_SINGLE_EMPTY = "F1";
  private static final String RANGE_BLOCK = "A1:C3";
  private static final String RANGE_BLOCK_EMPTY = "G2:H4";
  private static final String RANGE_ROW = "2";
  private static final String RANGE_COLUMN = "C";
  //TODO: add invalid range options
  
  private CloudDocClient cloudDocClient;
  private CloudSheet cloudSheet;

  @Before
  public void setUp() {
    cloudDocClient = new DriveClient();
    try {
      cloudSheet = cloudDocClient.getCloudSheet(SPREADSHEET_ID);
    } catch (IOException e) {
      Assert.fail(e.toString());
    } catch (GeneralSecurityException e) {
      Assert.fail(e.toString());
    }
  }

  @Test
  public void testReadRangeSingle() {
    List<List<String>> response = cloudSheet.readRange(
        String.format("%s!%s:%s", SHEET_NAME, RANGE_SINGLE, RANGE_SINGLE));

    String[] array = {"B1"};
    List<List<String>> expected = Arrays.asList(
      Arrays.asList(array)
    );

    Assert.assertEquals(expected, response);
  }

  @Test
  public void testReadRangeSingleEmpty() {
    List<List<String>> response = cloudSheet.readRange(
        String.format("%s!%s:%s", SHEET_NAME, RANGE_SINGLE_EMPTY, RANGE_SINGLE_EMPTY));
    Assert.assertEquals(null, response);
  }

  
}
