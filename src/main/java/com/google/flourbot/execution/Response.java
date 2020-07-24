/*
 * Helps control what card is displayed by bot after macro execution.
 */

package com.google.flourbot.execution;

// TODO: Populate with enum, grid, image options
public class Response {

  private final String replyText;

  public Response(String replyText) {
    this.replyText = replyText;
  }
}