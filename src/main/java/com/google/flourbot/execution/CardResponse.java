/*
 * Creates card response for bot
 */

package com.google.flourbot.execution;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.services.chat.v1.model.ActionParameter;
import com.google.api.services.chat.v1.model.Button;
import com.google.api.services.chat.v1.model.Card;
import com.google.api.services.chat.v1.model.CardHeader;
import com.google.api.services.chat.v1.model.FormAction;
import com.google.api.services.chat.v1.model.Image;
import com.google.api.services.chat.v1.model.ImageButton;
import com.google.api.services.chat.v1.model.KeyValue;
import com.google.api.services.chat.v1.model.Message;
import com.google.api.services.chat.v1.model.OnClick;
import com.google.api.services.chat.v1.model.OpenLink;
import com.google.api.services.chat.v1.model.Section;
import com.google.api.services.chat.v1.model.TextButton;
import com.google.api.services.chat.v1.model.TextParagraph;
import com.google.api.services.chat.v1.model.WidgetMarkup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class CardResponse {

  private static final String INTERACTIVE_TEXT_BUTTON_ACTION = "doTextButtonAction";
  private static final String INTERACTIVE_IMAGE_BUTTON_ACTION = "doImageButtonAction";
  private static final String INTERACTIVE_BUTTON_KEY = "originalMessage";
  private static final String HEADER_IMAGE = "https://goo.gl/5obRKj";
  private static final String BOT_NAME = "Card Bot Java";
  private static final String REDIRECT_URL = "https://goo.gl/kwhSNz";

  /**
    * Creates a card-formatted response based on the message sent in Hangouts Chat.
    *
    * @param message the event object sent from Hangouts Chat
    * @return a card instance
    */
  public static Card createCardResponse(String message) {
    Card card = new Card();
    List<WidgetMarkup> widgets = new ArrayList<>();
    List<ActionParameter> customParameters = Collections.singletonList(
            new ActionParameter().setKey(INTERACTIVE_BUTTON_KEY).setValue(message)
    );

    // Parse the message and add widgets to the responseStr in the order that
    // they were requested in the message.
    Stream.of(message.split(" ")).forEach((s -> {
        if (s.contains("header")) {
          CardHeader header = new CardHeader()
                  .setTitle(BOT_NAME)
                  .setSubtitle("Card header")
                  .setImageUrl(HEADER_IMAGE)
                  .setImageStyle("IMAGE");
          card.setHeader(header);
        } else if (s.contains("textparagraph")) {
          TextParagraph widget = new TextParagraph().setText("<b>This</b> is a <i>text paragraph</i>.");
          widgets.add(new WidgetMarkup().setTextParagraph(widget));
        } else if (s.contains("keyvalue")) {
          KeyValue widget = new KeyValue()
                  .setTopLabel("KeyValue widget")
                  .setContent("This is a KeyValue widget")
                  .setBottomLabel("The bottom label")
                  .setIcon("STAR");
          widgets.add(new WidgetMarkup().setKeyValue(widget));
        } else if (s.contains("interactivetextbutton")) {
          FormAction action = new FormAction()
                  .setActionMethodName(INTERACTIVE_TEXT_BUTTON_ACTION)
                  .setParameters(customParameters);
          OnClick onClick = new OnClick().setAction(action);
          TextButton button = new TextButton()
                  .setText("INTERACTIVE BUTTON")
                  .setOnClick(onClick);
          Button widget = new Button().setTextButton(button);
          widgets.add(new WidgetMarkup().setButtons(Collections.singletonList((widget))));
        } else if (s.contains("interactiveimagebutton")) {
          FormAction action = new FormAction()
                  .setActionMethodName(INTERACTIVE_IMAGE_BUTTON_ACTION)
                  .setParameters(customParameters);
          OnClick onClick = new OnClick().setAction(action);
          ImageButton button = new ImageButton()
                  .setIcon("EVENT_SEAT")
                  .setOnClick(onClick);
          Button widget = new Button().setImageButton(button);
          widgets.add(new WidgetMarkup().setButtons(Collections.singletonList((widget))));
        } else if (s.contains("textbutton")) {
          OpenLink openLink = new OpenLink().setUrl(REDIRECT_URL);
          OnClick onClick = new OnClick().setOpenLink(openLink);
          TextButton button = new TextButton()
                  .setText("TEXT BUTTON")
                  .setOnClick(onClick);
          Button widget = new Button().setTextButton(button);
          widgets.add(new WidgetMarkup().setButtons(Collections.singletonList((widget))));
        } else if (s.contains("imagebutton")) {
          OpenLink openLink = new OpenLink().setUrl(REDIRECT_URL);
          OnClick onClick = new OnClick().setOpenLink(openLink);
          ImageButton button = new ImageButton()
                  .setIcon("EVENT_SEAT")
                  .setOnClick(onClick);
          Button widget = new Button().setImageButton(button);
          widgets.add(new WidgetMarkup().setButtons(Collections.singletonList((widget))));
        } else if (s.contains("image")) {
          Image widget = new Image().setImageUrl("https://goo.gl/Bpa3Y5");
          widgets.add(new WidgetMarkup().setImage(widget));
        }
      }));

      Section section = new Section()
              .setWidgets(widgets);
      card.setSections(Collections.singletonList(section));
      return card;
  }

  /**
    * Handles the click for an interactive button.
    *
    * @param actionName name of action invoked
    * @param customParameterValue custom payload from event
    * @return a response card
    */
  public static Card respondToInteractiveCardClick(String actionName, String customParameterValue) {
    // Determine which button the user clicked.
    String message = String.format("You clicked <u>%s</u>. <br> Your original message was \"%s\".",
            actionName.equals(INTERACTIVE_TEXT_BUTTON_ACTION) ? "a TextButton" : "an ImageButton",
            customParameterValue);

    Card card = new Card();
    CardHeader header = new CardHeader();
    header.setTitle(BOT_NAME)
            .setSubtitle("Interactive card click")
            .setImageUrl(HEADER_IMAGE)
            .setImageStyle("IMAGE");
    card.setHeader(header);
    TextParagraph text = new TextParagraph().setText(message);
    List<WidgetMarkup> widgets = Collections.singletonList
            (new WidgetMarkup().setTextParagraph(text));
    Section section = new Section()
            .setWidgets(widgets);
    card.setSections(Collections.singletonList(section));
    return card;
  }
}