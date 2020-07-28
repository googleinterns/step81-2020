/*
 * Creates service with Google Sheets API. Service is used to read/write documents.
 */

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

public class DriveClient implements CloudDocClient {

  static final String SPREADSHEET_SCOPE = "https://www.googleapis.com/auth/spreadsheets";

  public CloudSheet getCloudSheet(String documentId) throws IOException, GeneralSecurityException {
    return new DriveCloudSheet(documentId, this.createService());
  }

  private Sheets createService() throws IOException, GeneralSecurityException {
    
    // Set up credentials
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    GoogleCredentials credentials = GoogleCredentials.fromStream(
            DriveClient.class.getResourceAsStream("/service-acct.json")
    ).createScoped(SPREADSHEET_SCOPE);
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