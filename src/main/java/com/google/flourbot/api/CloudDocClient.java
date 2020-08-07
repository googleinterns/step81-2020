package com.google.flourbot.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
/**
 * Interface for module that creates service with Google Sheets API. 
 */
public interface CloudDocClient {
  /**
  * Constructs the CloudSheet object using the service
  * @param documentId the id of the Google Sheets document to read/write
  * @return a CloudSheet object with read/write methods
  */
  public CloudSheet getCloudSheet(String documentId);
}
