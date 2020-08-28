package com.google.flourbot.api;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Logger;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Controls read/write logic with provided sheetId and sheetsService.
 */
public class DriveCloudSheet implements CloudSheet {
  private static final String VALUE_INPUT_OPTION = "USER_ENTERED";
  private static final String INSERT_DATA_OPTION = "INSERT_ROWS";
  private static final String SHEET_NAME = "Sheet1"; // Stretch goal: allow user specified
  private Sheets sheetsService;
  private String spreadsheetId;
  
  /** 
  * Constructor
  * @param documentId -> the id of the Google Sheets Document
  * @param documentService -> the Sheets object that's already set up to have the proper
  *             Google Service Key credentials to set up an HTTP endpoint with the Sheets API
  * @return DriveCloudSheet object
  */
  public DriveCloudSheet(String documentId, Sheets documentService) {
    this.spreadsheetId = documentId;
    this.sheetsService = documentService;
  }

  /**
  * Calculates a range based off of the number of columns needed for the data and the number preskipped
  * @param numEntryColumns is the length of the data (number of cells needed)
  * @param numSkipColumns is the number of columns to the left we are skipping (e.g. 1 means start
  *     the range at column B not A)
  * @return the range in A1 format (e.g. "Sheet1!B3:B9")
  */
  private String getRange(int numEntryColumns, int numSkipColumns) {
    String startColumn = toAlphabetic(numSkipColumns);
    String endColumn = toAlphabetic(numSkipColumns + numEntryColumns - 1);
    return String.format("%s!%s:%s", SHEET_NAME, startColumn, endColumn);
  }

  /**
  * Recursive strategy to build a string that represents the alphabetic name
  * @param i is column number
  * @return the letter corresponding to that column (e.g. 0 -> A, 1 -> B... 25 -> Z, 26 -> AA..)
  */
  public static String toAlphabetic(int i) {
    // Negative numbers cause errors
    if (i < 0) {
      throw new IllegalArgumentException(String.format("Column number %d is invalid", i));
    }

    // Every calculation produces 1 letter
    int quotient = i / 26;
    char letter = (char)(i % 26 + 'A');
    if (quotient == 0) {
        return "" + letter;
    } else {
        return toAlphabetic(quotient-1) + letter;
    }
  }

  /**
    * Method generates and sends request read a Range in A1 notation
    * @param range notation for CELLS (no sheet name) (e.g. "A1:C3")
    * @return nested list of strings, where each string is the cell contents, and the inner lists
    *         represents each row
    */
  public List<List<String>> readRange(String range){
    ValueRange response;
    try {
      response = sheetsService.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }

    return (List<List<String>>)((Object)response.getValues());
  }

  /**
    * Method generates and sends request to read a row
    * @param row is the number representing the row desired
    * @return nested list of strings for the row, where each string is the cell contents
    */
  public List<String> readRow(int row) {
    List<List<String>> values = readRange(String.format("%s!%d:%d", SHEET_NAME, row, row));

    if (values != null || !values.isEmpty()) {
      return (List<String>) values.get(0);
    }
    return Collections.emptyList();
  }

   /**
    * Method generates and sends request to read a column
    * @param column is the String representing the column desired (e.g. "B")
    * @return nested list of strings for the column, where each string is the cell contents
    */
  public List<String> readColumn(String column) {
    List<List<String>> values = readRange(String.format("%s!%s:%s", SHEET_NAME, column, column));

    if (values != null || !values.isEmpty()) {
      return (List<String>) values.stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }

  /**
    * Method generates and sends request to read a sheet
    * @param sheetName is the String representing the sheet desired (e.g. "Sheet1")
    * @return nested list of strings, where each string is the cell contents, and the inner lists
    *         represents each row
    */
  public List<List<String>> readSheet(String sheetName) {
    return readRange(sheetName);
  }

  /**
    * Method generates and sends request to append rows to bottom of sheet containing values
    * @param values where each string goes into the next cell of the new row
    * @return void
    */
  public void appendRow(List<String> values) throws IOException, GeneralSecurityException {
    // Method generates and sends request to append rows to bottom of sheet containing values

    // Calculate the range where the values will be inserted
    String range = getRange(values.size(), 0);
    
    // Convert the values into a List of Lists, which is needed to send the request
    String[] array = values.stream().toArray(String[]::new);
    List<List<Object>> valuesList = Arrays.asList(
      Arrays.asList(array)
    );

    ValueRange requestBody = new ValueRange().setValues(valuesList);

    // Send request
    Sheets.Spreadsheets.Values.Append request =
      this.sheetsService.spreadsheets().values().append(spreadsheetId, range, requestBody);
    request.setValueInputOption(VALUE_INPUT_OPTION);
    request.setInsertDataOption(INSERT_DATA_OPTION);
    
    AppendValuesResponse response = request.execute();
  }
}