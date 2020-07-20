package com.google.flourbot.api;

import com.google.api.services.sheets.v4.*;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Logger;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DriveCloudSheet implements CloudSheet {
  Sheets sheetsService;
  String spreadsheetId;

  public DriveCloudSheet(String documentId, Sheets documentService) {
    this.spreadsheetId = documentId;
    this.sheetsService = documentService;
  }

  // TODO: replace hardcoded range with calculated range (so far values must be length 3)
  public void appendRow(List<String> values) {
    
    // Generate request to append to sheet
    String range = "Sheet1!A1:C1";
    String valueInputOption = "USER_ENTERED";
    String insertDataOption = "INSERT_ROWS";

    ValueRange requestBody = new ValueRange();
    
    String[] array = values.stream().toArray(String[]::new);
    List<List<Object>> v = Arrays.asList(
      Arrays.asList(array)
    );
    
    requestBody.setValues(v);

    try {
      Sheets.Spreadsheets.Values.Append request =
              this.sheetsService.spreadsheets().values().append(this.spreadsheetId, range, requestBody);
      request.setValueInputOption(valueInputOption);
      request.setInsertDataOption(insertDataOption);

      AppendValuesResponse response = request.execute();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }

  }
  
  // May not use this method
  public String createNew() {

    try {
      // make a new sheet and tell the user the ID and URL
      Spreadsheet requestBody = new Spreadsheet()
                    .setProperties(new SpreadsheetProperties()
                      .setTitle("TEST"));;
      Sheets.Spreadsheets.Create request = this.sheetsService.spreadsheets().create(requestBody);
      Spreadsheet response = request.execute();

      return response.getSpreadsheetId();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}