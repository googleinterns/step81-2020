/*
 * Helps control what card is displayed by bot after macro execution.
 */

package com.google.flourbot.execution;

import com.google.flourbot.entity.action.ActionType;

import java.util.List;

public class ChatResponse {
  
  private final String replyText;
  private final ActionType actionType;
  private final String documentUrl;

  public ChatResponse(String replyText) {
    // Constructor for response for failed actions
    this.replyText = replyText;
  }

  public ChatResponse(String replyText, ActionType actionType, String documentUrl) {
    // Constructor for response for successful actions
    this.replyText = replyText;
    this.actionType = actionType;
    this.documentUrl = documentUrl;
  }

  public ChatResponse(List<String> replyText, ActionType actionType, String documentUrl) {
    // Overloaded
    this.replyText = listToString(replyText, actionType);
    this.actionType = actionType;
    this.documentUrl = documentUrl;
  }

  public ChatResponse(List<List<String>> replyText, ActionType actionType, String documentUrl) {
    // Overloaded
    this.replyText = listListToString(replyText, actionType);
    this.actionType = actionType;
    this.documentUrl = documentUrl;
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

  private String listToString (List<String> values, ActionType actionType) {
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

  private String listToHtmlTable (List<String> values, String dimension) {
    // Converts list of values to HTML tables

    // Open table
    StringBuilder htmlTable = new StringBuilder("<table>");

    // If the values represent 1 row, open the row using <tr>, and add a <td> tag for every entry in the row
    if (dimension == "ROW") {
      htmlTable.append("<tr>");
      for (String v: values) {
        htmlTable.append(String.format("<td>%s</td>", v));
      }
      htmlTable.append("</tr>");

    // If the values represent 1 column, create a new row <tr> and <td> tag for every entry
    } else if (dimension == "COLUMN") {
      for (String v: values) {
        htmlTable.append(String.format("<tr><td>%s</td></tr>", v));
      }
    }

    // Close table tag
    htmlTable.append("</table>");

    return htmlTable.toString();
  }

  private String listListToString (List<List<String>> values, ActionType actionType) {
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

    private String listListToHtmlTable (List<List<String>> values) {
    // Converts list of values to HTML tables

    // Open table
    StringBuilder htmlTable = new StringBuilder("<table>");

    // For row
    for (List ls: values) {
      htmlTable.append("<tr>");

      // For col
      for (String s: ls) {
        htmlTable.append(String.format("<td>%s</td>", s));
      }
      htmlTable.append("</tr>");
    }

    htmlTable.append("</table>");
    return htmlTable.toString();
  }
}
