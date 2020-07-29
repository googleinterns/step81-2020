/*
 * Helps control what card is displayed by bot after macro execution.
 */

package com.google.flourbot.execution;

import com.google.flourbot.entity.action.ActionType;

import java.util.Collection;
import java.util.List;

public class ChatResponse {
  
  private final String replyText;
  private final ActionType actionType;
  private final String documentUrl;

  public ChatResponse(String replyText) {
    // Constructor for response for failed actions
    this.replyText = replyText;
    this.actionType = null;
    this.documentUrl = null;
  }

  public ChatResponse(String replyText, ActionType actionType, String documentUrl) {
    // Constructor for response for successful actions
    this.replyText = replyText;
    this.actionType = actionType;
    this.documentUrl = documentUrl;
  }

  public static ChatResponse createChatResponseWithList(List<String> replyText, ActionType actionType, String documentUrl) {
    return new ChatResponse(listToString(replyText, actionType), actionType, documentUrl);
  }
  
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

  private static String listToString (List<String> values, ActionType actionType) {
    // Retrieves appropriate HTML string to display values in card response
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

  private static String listToHtmlTable (List<String> values, String dimension) {
    // Converts list of values to HTML tables

    // Open table
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

  private static String listListToString (List<List<String>> values, ActionType actionType) {
    // Retrieves appropriate HTML string to display values in card response
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

  private static String listListToHtmlTable (List<List<String>> values) {
    // Converts list of values to HTML tables

    // Open table
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
