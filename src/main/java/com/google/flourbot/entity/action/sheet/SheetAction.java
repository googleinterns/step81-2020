package com.google.flourbot.entity.action.sheet;

import com.google.flourbot.entity.action.Action;
import com.google.flourbot.entity.action.ActionType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SheetAction extends Action {
  
  private static final String REGEX_PATTERN = "(d\\/[a-zA-Z0-9-_]+)";
  protected final String documentUrl;

  public SheetAction(String documentUrl) {
    this.documentUrl = documentUrl;
  }

  public abstract ActionType getActionType();

  public String getDocumentUrl() {
    return documentUrl;
  }

  public String getDocumentId() {
    String documentId = "";
    Pattern pattern = Pattern.compile(REGEX_PATTERN);
    Matcher matcher = pattern.matcher(documentUrl);
    // Take the first occurance
    if (matcher.find()) {
      documentId = matcher.group(0).substring(2);
    }
    return documentId;
  }
}
