package com.google.flourbot.api;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.Get;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import static org.mockito.BDDMockito.when;

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
  private static final String SPREADSHEET_ID = "123";
  private static final String SHEET_NAME = "Sheet1";

  private static final String RANGE_SINGLE = "B1:B1";
  private static final String RANGE_SINGLE_EMPTY = "F1:F1";
  private static final String RANGE_BLOCK = "A1:C3";
  private static final String RANGE_BLOCK_EMPTY = "G2:H4";
  private static final String RANGE_ROW = "2:2";
  private static final String RANGE_COLUMN = "C:C";

  private static final int ROW = 3;
  private static final String COLUMN = "D";

  private static final int NUM_SMALL = 5;
  private static final int NUM_BIG = 88;
  
  private Sheets sheetsServiceMock;
  private Spreadsheets spreadsheetsMock;
  private Values valuesMock;
  private Get getMock;
  private ValueRange valueRangeMock;
  private CloudSheet cloudSheet;

  @Before
  public void setUp() {
    sheetsServiceMock = Mockito.mock(Sheets.class);
    spreadsheetsMock = Mockito.mock(Spreadsheets.class);
    valuesMock = Mockito.mock(Values.class);
    getMock = Mockito.mock(Get.class);
    valueRangeMock  = Mockito.mock(ValueRange.class);
    cloudSheet = new DriveCloudSheet(SPREADSHEET_ID, sheetsServiceMock);
  }

  @Test
  public void testReadRangeSingle() {
    String[] array = {"B1"};
    List<List<Object>> values = Arrays.asList(
      Arrays.asList(array)
    );

    when(sheetsServiceMock.spreadsheets()).thenReturn(spreadsheetsMock);
    when(spreadsheetsMock.values()).thenReturn(valuesMock);
    try {
      when(valuesMock.get(SPREADSHEET_ID, String.format("%s!%s", SHEET_NAME, RANGE_SINGLE))).thenReturn(getMock);
      when(getMock.execute()).thenReturn(valueRangeMock);
    } catch (IOException e) {}

    when(valueRangeMock.getValues()).thenReturn(values);

    List<List<String>> response = cloudSheet.readRange(
        String.format("%s!%s", SHEET_NAME, RANGE_SINGLE));


    List<List<String>> expected = Arrays.asList(
      Arrays.asList(array)
    );

    Assert.assertEquals(expected, response);
  }

  @Test
  public void testReadRangeSingleEmpty() {
    when(sheetsServiceMock.spreadsheets()).thenReturn(spreadsheetsMock);
    when(spreadsheetsMock.values()).thenReturn(valuesMock);
    try {
      when(valuesMock.get(SPREADSHEET_ID, String.format("%s!%s", SHEET_NAME, RANGE_SINGLE_EMPTY))).thenReturn(getMock);
      when(getMock.execute()).thenReturn(valueRangeMock);
    } catch (IOException e) {}

    when(valueRangeMock.getValues()).thenReturn(null);

    List<List<String>> response = cloudSheet.readRange(
        String.format("%s!%s", SHEET_NAME, RANGE_SINGLE_EMPTY));
    Assert.assertEquals(null, response);
  }

  @Test
  public void testReadRangeBlock() {
    String[] v1 = {"A1", "B1", "C1"};
    String[] v2 = {"A2", "B2", "C2"};
    String[] v3 = {"A3", "B3", "C3"};
    List<List<Object>> values = Arrays.asList(
      Arrays.asList(v1),
      Arrays.asList(v2),
      Arrays.asList(v3)
    );

    when(sheetsServiceMock.spreadsheets()).thenReturn(spreadsheetsMock);
    when(spreadsheetsMock.values()).thenReturn(valuesMock);
    try {
      when(valuesMock.get(SPREADSHEET_ID, String.format("%s!%s", SHEET_NAME, RANGE_BLOCK))).thenReturn(getMock);
      when(getMock.execute()).thenReturn(valueRangeMock);
    } catch (IOException e) {}

    when(valueRangeMock.getValues()).thenReturn(values);

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
    when(sheetsServiceMock.spreadsheets()).thenReturn(spreadsheetsMock);
    when(spreadsheetsMock.values()).thenReturn(valuesMock);
    try {
      when(valuesMock.get(SPREADSHEET_ID, String.format("%s!%s", SHEET_NAME, RANGE_BLOCK_EMPTY))).thenReturn(getMock);
      when(getMock.execute()).thenReturn(valueRangeMock);
    } catch (IOException e) {}

    when(valueRangeMock.getValues()).thenReturn(null);

    List<List<String>> response = cloudSheet.readRange(
        String.format("%s!%s", SHEET_NAME, RANGE_BLOCK_EMPTY));
    Assert.assertEquals(null, response);
  }

  @Test
  public void testReadRangeRow() {
    String[] array = {"A2", "B2", "C2", "D2", "E2"};
    List<List<Object>> values = Arrays.asList(
      Arrays.asList(array)
    );

    when(sheetsServiceMock.spreadsheets()).thenReturn(spreadsheetsMock);
    when(spreadsheetsMock.values()).thenReturn(valuesMock);
    try {
      when(valuesMock.get(SPREADSHEET_ID, String.format("%s!%s", SHEET_NAME, RANGE_ROW))).thenReturn(getMock);
      when(getMock.execute()).thenReturn(valueRangeMock);
    } catch (IOException e) {}

    when(valueRangeMock.getValues()).thenReturn(values);

    List<List<String>> response = cloudSheet.readRange(
        String.format("%s!%s", SHEET_NAME, RANGE_ROW));

    List<List<String>> expected = Arrays.asList(
      Arrays.asList(array)
    );

    Assert.assertEquals(expected, response);
  }

  @Test
  public void testReadRangeColumn() {
    String[] array1 = {"C1"};
    String[] array2 = {"C2"};
    String[] array3 = {"C3"};
    String[] array4 = {"C4"};
    String[] array5 = {"C5"};

    List<List<Object>> values = Arrays.asList(
      Arrays.asList(array1),
      Arrays.asList(array2),
      Arrays.asList(array3),
      Arrays.asList(array4),
      Arrays.asList(array5)
    );

    when(sheetsServiceMock.spreadsheets()).thenReturn(spreadsheetsMock);
    when(spreadsheetsMock.values()).thenReturn(valuesMock);
    try {
      when(valuesMock.get(SPREADSHEET_ID, String.format("%s!%s", SHEET_NAME, RANGE_COLUMN))).thenReturn(getMock);
      when(getMock.execute()).thenReturn(valueRangeMock);
    } catch (IOException e) {}

    when(valueRangeMock.getValues()).thenReturn(values);

    List<List<String>> response = cloudSheet.readRange(
        String.format("%s!%s", SHEET_NAME, RANGE_COLUMN));

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
    String[] array = {"A3", "B3", "C3", "D3", "E3"};
    List<List<Object>> values = Arrays.asList(
      Arrays.asList(array)
    );

    when(sheetsServiceMock.spreadsheets()).thenReturn(spreadsheetsMock);
    when(spreadsheetsMock.values()).thenReturn(valuesMock);
    try {
      when(valuesMock.get(SPREADSHEET_ID, String.format("%s!%s:%s", SHEET_NAME, ROW, ROW))).thenReturn(getMock);
      when(getMock.execute()).thenReturn(valueRangeMock);
    } catch (IOException e) {}

    when(valueRangeMock.getValues()).thenReturn(values);

    List<String> response = cloudSheet.readRow(ROW);
    
    List<String> expected = Arrays.asList(array);
    Assert.assertEquals(expected, response);
  }

  @Test
  public void testReadColumn() {
    String[] array1 = {"D1"};
    String[] array2 = {"D2"};
    String[] array3 = {"D3"};
    String[] array4 = {"D4"};
    String[] array5 = {"D5"};

    List<List<Object>> values = Arrays.asList(
      Arrays.asList(array1),
      Arrays.asList(array2),
      Arrays.asList(array3),
      Arrays.asList(array4),
      Arrays.asList(array5)
    );

    when(sheetsServiceMock.spreadsheets()).thenReturn(spreadsheetsMock);
    when(spreadsheetsMock.values()).thenReturn(valuesMock);
    try {
      when(valuesMock.get(SPREADSHEET_ID, String.format("%s!%s:%s", SHEET_NAME, COLUMN, COLUMN))).thenReturn(getMock);
      when(getMock.execute()).thenReturn(valueRangeMock);
    } catch (IOException e) {}

    when(valueRangeMock.getValues()).thenReturn(values);

    List<String> response = cloudSheet.readColumn(COLUMN);
    String[] array = {"D1", "D2", "D3", "D4", "D5"};
    List<String> expected = Arrays.asList(array);
    Assert.assertEquals(expected, response);
  }

  @Test
  public void testReadSheet() {
    String[] array1 = {"A1", "B1", "C1", "D1", "E1"};
    String[] array2 = {"A2", "B2", "C2", "D2", "E2"};
    String[] array3 = {"A3", "B3", "C3", "D3", "E3"};
    String[] array4 = {"A4", "B4", "C4", "D4", "E4"};
    String[] array5 = {"A5", "B5", "C5", "D5", "E5"};

    List<List<Object>> values = Arrays.asList(
      Arrays.asList(array1),
      Arrays.asList(array2),
      Arrays.asList(array3),
      Arrays.asList(array4),
      Arrays.asList(array5)
    );

    when(sheetsServiceMock.spreadsheets()).thenReturn(spreadsheetsMock);
    when(spreadsheetsMock.values()).thenReturn(valuesMock);
    try {
      when(valuesMock.get(SPREADSHEET_ID, String.format("%s", SHEET_NAME))).thenReturn(getMock);
      when(getMock.execute()).thenReturn(valueRangeMock);
    } catch (IOException e) {}

    when(valueRangeMock.getValues()).thenReturn(values);

    List<List<String>> response = cloudSheet.readSheet(SHEET_NAME);

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
  public void testToAlphabeticSmall() {
    String response = DriveCloudSheet.toAlphabetic(NUM_SMALL);
    String expected = "F";
    Assert.assertEquals(expected, response);
  }

  @Test
  public void testToAlphabeticBig() {
    String response = DriveCloudSheet.toAlphabetic(NUM_BIG);
    String expected = "CK";
    Assert.assertEquals(expected, response);
  }
}
