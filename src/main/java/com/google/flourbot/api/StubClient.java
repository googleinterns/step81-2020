package com.google.flourbot.api;

public class StubClient implements CloudDocClient{
  private CloudSheet stubSheet;

  public StubClient(CloudSheet stubSheet) {
    this.stubSheet = stubSheet;
  }

  @Override
  public CloudSheet getCloudSheet(String documentId){
    return this.stubSheet;
  }
}
