package com.google.flourbot.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
 * Creates service with Google Sheets API. Service is used to read/write documents.
 */
public class DriveClient implements CloudDocClient {

  static final String SPREADSHEET_SCOPE = "https://www.googleapis.com/auth/spreadsheets";

  /**
  * Constructs the CloudSheet object using the service
  * @param documentId the id of the Google Sheets document to read/write
  * @return a CloudSheet object with read/write methods
  */
  public CloudSheet getCloudSheet(String documentId) {
    return new DriveCloudSheet(documentId, this.createService());
  }
  
  /**
  * Establishes a HTTP connection to the Google Sheets v4 RESTful API
  * and authenticates using Google Cloud Platform service account key from resources folder
  * @param none
  * @return Sheets object that can be used for API calls to documents
  */
  private Sheets createService() {
    
    // Set up credentials
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    NetHttpTransport httpTransport = null;
    GoogleCredentials credentials = null;

    try {
      httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException(e);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }

    try {
      credentials = GoogleCredentials.fromStream(
              DriveClient.class.getResourceAsStream("/service-acct.json")
          ).createScoped(SPREADSHEET_SCOPE);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }

    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

    // Create sheetService
    return new Sheets.Builder(
            httpTransport,
            jsonFactory,
            requestInitializer)
          .setApplicationName("bot-sheets")
          .build();
  }
}