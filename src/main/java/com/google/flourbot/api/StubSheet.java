package com.google.flourbot.api;

import java.util.List;

public class StubSheet implements CloudSheet{

  private List<List<String>> mock2Dvalues;
  private List<String> mock1Dvalues;

  public void setMock1Dvalues(List<String> mock1Dvalues) {
    this.mock1Dvalues = mock1Dvalues;
  }

  public void setMock2Dvalues(List<List<String>> mock2Dvalues) {
    this.mock2Dvalues = mock2Dvalues;
  }

  @Override
  public void appendRow(List<String> values){}

  @Override
  public List<List<String>> readSheet(String sheetName) {
    return mock2Dvalues;
  }

  @Override
  public List<List<String>> readRange(String range) {
    return mock2Dvalues;
  }

  @Override
  public List<String> readRow(int row) {
    return mock1Dvalues;
  }

  @Override
  public List<String> readColumn(String column) {
    return mock1Dvalues;
  }
}
