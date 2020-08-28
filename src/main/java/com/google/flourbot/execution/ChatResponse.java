package com.google.flourbot.execution;

import com.google.flourbot.entity.action.ActionType;

import java.util.Collection;
import java.util.List;
/*
 * Helps control what card is displayed by bot after macro execution.
 */
public class ChatResponse {
  
  private final String replyText;
  private final ActionType actionType;
  private final String documentUrl;

  /**
  * Constructor for response for failed actions
  * @param replyText the message for the bot to send back to chat as a text message
  */
  public ChatResponse(String replyText) {
    this.replyText = replyText;
    this.actionType = null;
    this.documentUrl = null;
  }

  /**
  * Constructor for response for successful actions
  * @param replyText the message for the bot to send back to chat as a card message
  * @param actionType the specific read/write action that was just executed (unused)
  * @param documentUrl the https://......com of the Google Drive document
  */
  public ChatResponse(String replyText, ActionType actionType, String documentUrl) {
    this.replyText = replyText;
    this.actionType = actionType;
    this.documentUrl = documentUrl;
  }

  /**
  * Calls constructor for response for successful actions
  * @param replyText a list needing formatting to become a message for the bot to send 
  *  back to chat as a card message
  * @param actionType the specific read/write action that was just executed (unused)
  * @param documentUrl the https://......com of the Google Drive document
  */
  public static ChatResponse createChatResponseWithList(List<String> replyText, ActionType actionType, String documentUrl) {
    return new ChatResponse(listToString(replyText, actionType), actionType, documentUrl);
  }
  
  /**
  * Calls constructor for response for successful actions
  * @param replyText a list of lists needing formatting to become a message for the bot to send 
  *  back to chat as a card message
  * @param actionType the specific read/write action that was just executed (unused)
  * @param documentUrl the https://......com of the Google Drive document
  */
  public static ChatResponse createChatResponseWithListList(List<List<String>> replyText, ActionType actionType, String documentUrl) {
    return new ChatResponse(listListToString(replyText, actionType), actionType, documentUrl);
  }

  public ActionType getActionType() {
    return actionType;
  }

  public String getReplyText() {
    return replyText;
  }

  public String getDocumentUrl() {
    return documentUrl;
  }

  /** 
  * Retrieves appropriate HTML string to display values in card response
  */
  private static String listToString (List<String> values, ActionType actionType) {
    String replyText = "";

    switch (actionType) {
      case SHEET_APPEND_ROW:
      case SHEET_READ_ROW:
        replyText = listToHtmlTable(values, "ROW");
        break;

      case SHEET_APPEND_COLUMN:
      case SHEET_READ_COLUMN:
        replyText = listToHtmlTable(values, "COLUMN");
        break;
      default:
        throw new IllegalStateException("Action type not recognized");
    }
    return replyText;
  }

  /** 
  * Builds a string using a list where the string resembles loosely a HTML table
  */
  private static String listToHtmlTable (List<String> values, String dimension) {
    StringBuilder htmlTable = new StringBuilder();

    // If the values represent 1 row, open row using <br>, and add a | tag for every entry in the row
    if (dimension == "ROW") {
      htmlTable.append("<br></br>");
      for (String v: values) {
        htmlTable.append(String.format(" %s |", v));
      }
      htmlTable.append("<br></br>");

    // If the values represent 1 column, create a new row <tr> and <td> tag for every entry
    } else if (dimension == "COLUMN") {
      for (String v: values) {
        htmlTable.append(String.format("<br></br> %s <br></br>", v));
      }
    }
    
    return htmlTable.toString();
  }

  /** 
  * Retrieves appropriate HTML string to display values in card response as a table
  */
  private static String listListToString (List<List<String>> values, ActionType actionType) {
    String replyText = "";

    switch (actionType) {
      case SHEET_READ_SHEET:
        replyText = listListToHtmlTable(values);
        break;
      
      default:
        throw new IllegalStateException("Action type not recognized");
    }
    return replyText;
  }

  /** 
  * Builds a string using a list of lists where the string resembles loosely a HTML table
  */
  private static String listListToHtmlTable (List<List<String>> values) {
    StringBuilder htmlTable = new StringBuilder();

    // For row
    for (List<String> ls: values) {
      htmlTable.append("<br></br>");

      // For col
      for (String s: ls) {
        htmlTable.append(String.format(" %s |", s));
      }
      htmlTable.append("<br></br>");
    }

    return htmlTable.toString();
  }
}
