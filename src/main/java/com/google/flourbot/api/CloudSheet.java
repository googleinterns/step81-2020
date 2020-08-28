package com.google.flourbot.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
/**
 * Interface for module that reads/writes to sheet documents.
 */
public interface CloudSheet {
  /**
    * Method generates and sends request read a Range in A1 notation
    * @param range notation for CELLS (no sheet name) (e.g. "A1:C3")
    * @return nested list of strings, where each string is the cell contents, and the inner lists
    *         represents each row
    */
  public List<List<String>> readRange(String range);

  /**
    * Method generates and sends request to read a row
    * @param row is the number representing the row desired
    * @return nested list of strings for the row, where each string is the cell contents
    */
  public List<String> readRow(int row);

   /**
    * Method generates and sends request to read a column
    * @param column is the String representing the column desired (e.g. "B")
    * @return nested list of strings for the column, where each string is the cell contents
    */
  public List<String> readColumn(String column);

  /**
    * Method generates and sends request to read a sheet
    * @param sheetName is the String representing the sheet desired (e.g. "Sheet1")
    * @return nested list of strings, where each string is the cell contents, and the inner lists
    *         represents each row
    */
  public List<List<String>> readSheet(String sheetName);

  /**
    * Method generates and sends request to append rows to bottom of sheet containing values
    * @param values where each string goes into the next cell of the new row
    * @return void
    */
  public void appendRow(List<String> values) throws IOException, GeneralSecurityException;
}