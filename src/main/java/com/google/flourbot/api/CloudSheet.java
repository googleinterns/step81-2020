/*
 * Interface for module that reads/writes to sheet documents.
 */
package com.google.flourbot.api;

import com.google.flourbot.entity.action.sheet.SelectionMethod;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public interface CloudSheet {
  // Method generates and sends request to append rows to bottom of sheet containing values
  public void appendRow(List<String> values) throws IOException, GeneralSecurityException;

  public List<List<String>> readRange(String range);

  public List<String> readRow(int row, SelectionMethod selectionMethod);
  public List<String> readColumn(String column, SelectionMethod selectionMethod);
  public List<List<String>> readSheet(String sheetName);
}