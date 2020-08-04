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
  private static final String SPREADSHEET_ID = "";
  private static final String SHEET_NAME = "Sheet1";

  private static final String RANGE_SINGLE = "B1:B1";
  private static final String RANGE_SINGLE_EMPTY = "F1:F1";
  private static final String RANGE_BLOCK = "A1:C3";
  private static final String RANGE_BLOCK_EMPTY = "G2:H4";
  private static final String RANGE_ROW = "2:2";
  private static final String RANGE_COLUMN = "C:C";

  private static final int ROW = 3;
  private static final String COLUMN = "D";

  
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
        String.format("%s!%s", SHEET_NAME, RANGE_SINGLE));

    String[] array = {"B1"};
    List<List<String>> expected = Arrays.asList(
      Arrays.asList(array)
    );

    Assert.assertEquals(expected, response);
  }

  @Test
  public void testReadRangeSingleEmpty() {
    List<List<String>> response = cloudSheet.readRange(
        String.format("%s!%s", SHEET_NAME, RANGE_SINGLE_EMPTY));
    Assert.assertEquals(null, response);
  }

  @Test
  public void testReadRangeBlock() {
    List<List<String>> response = cloudSheet.readRange(
        String.format("%s!%s", SHEET_NAME, RANGE_BLOCK));

    String[] array1 = {"A1", "B1", "C1"};
    String[] array2 = {"A2", "B2", "C2"};
    String[] array3 = {"A3", "B3", "C3"};
    List<List<String>> expected = Arrays.asList(
      Arrays.asList(array1),
      Arrays.asList(array2),
      Arrays.asList(array3)
    );

    Assert.assertEquals(expected, response);
  }

  @Test
  public void testReadRangeBlockEmpty() {
    List<List<String>> response = cloudSheet.readRange(
        String.format("%s!%s", SHEET_NAME, RANGE_BLOCK_EMPTY));
    Assert.assertEquals(null, response);
  }

  @Test
  public void testReadRangeRow() {
    List<List<String>> response = cloudSheet.readRange(
        String.format("%s!%s", SHEET_NAME, RANGE_ROW));

    String[] array = {"A2", "B2", "C2", "D2", "E2"};
    List<List<String>> expected = Arrays.asList(
      Arrays.asList(array)
    );

    Assert.assertEquals(expected, response);
  }

  @Test
  public void testReadRangeColumn() {
    List<List<String>> response = cloudSheet.readRange(
        String.format("%s!%s", SHEET_NAME, RANGE_COLUMN));

    String[] array1 = {"C1"};
    String[] array2 = {"C2"};
    String[] array3 = {"C3"};
    String[] array4 = {"C4"};
    String[] array5 = {"C5"};

    List<List<String>> expected = Arrays.asList(
      Arrays.asList(array1),
      Arrays.asList(array2),
      Arrays.asList(array3),
      Arrays.asList(array4),
      Arrays.asList(array5)
    );

    Assert.assertEquals(expected, response);
  }

  @Test
  public void testReadRow() {
    List<String> response = cloudSheet.readRow(ROW);
    String[] array = {"A3", "B3", "C3", "D3", "E3"};
    List<String> expected = Arrays.asList(array);
    Assert.assertEquals(expected, response);
  }

  @Test
  public void testReadColumn() {
    List<String> response = cloudSheet.readColumn(COLUMN);
    String[] array = {"D1", "D2", "D3", "D4", "D5"};
    List<String> expected = Arrays.asList(array);
    Assert.assertEquals(expected, response);
  }

  @Test
  public void testReadSheet() {
    List<List<String>> response = cloudSheet.readSheet(SHEET_NAME);
    String[] array1 = {"A1", "B1", "C1", "D1", "E1"};
    String[] array2 = {"A2", "B2", "C2", "D2", "E2"};
    String[] array3 = {"A3", "B3", "C3", "D3", "E3"};
    String[] array4 = {"A4", "B4", "C4", "D4", "E4"};
    String[] array5 = {"A5", "B5", "C5", "D5", "E5"};

    List<List<String>> expected = Arrays.asList(
      Arrays.asList(array1),
      Arrays.asList(array2),
      Arrays.asList(array3),
      Arrays.asList(array4),
      Arrays.asList(array5)
    );

    Assert.assertEquals(expected, response);
  }


}
