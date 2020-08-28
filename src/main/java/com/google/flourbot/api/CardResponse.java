package com.google.flourbot.api;

import com.google.flourbot.entity.action.ActionType;

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
/**
 * Creates card response for bot
 */
public class CardResponse {

  private static final String INTERACTIVE_BUTTON_KEY = "originalMessage";
  private static final String HEADER_IMAGE = "https://shop.googlemerchandisestore.com/store/20160512512/assets/items/largeimages/GGOEACBA116699.jpg";
  private static final String BOT_NAME = "MacroBot";

  /**
    * Creates a card-formatted response based on the message sent in Hangouts Chat.
    *
    * @param message the event object sent from Hangouts Chat
    * @return a card instance
    */
  public static Card createCardResponse(String message, ActionType actionType, String documentUrl) {
    Card card = new Card();
    List<WidgetMarkup> widgets = new ArrayList<>();
    List<ActionParameter> customParameters = Collections.singletonList(
            new ActionParameter().setKey(INTERACTIVE_BUTTON_KEY).setValue(message)
    );

    // HEADER
    CardHeader header = new CardHeader()
                  .setTitle(BOT_NAME)
                  .setImageUrl(HEADER_IMAGE)
                  .setImageStyle("IMAGE");
    card.setHeader(header);

    // TEXT PARAGRAPH
    TextParagraph textParagraphWidget = new TextParagraph().setText(message);
    widgets.add(new WidgetMarkup().setTextParagraph(textParagraphWidget));

    // TEXT BUTTON
    OpenLink openLink = new OpenLink().setUrl(documentUrl);
    OnClick onClick = new OnClick().setOpenLink(openLink);
    TextButton button = new TextButton()
                  .setText("OPEN SHEET")
                  .setOnClick(onClick);
    Button buttonWidget = new Button().setTextButton(button);
    widgets.add(new WidgetMarkup().setButtons(Collections.singletonList((buttonWidget))));
    
    Section section = new Section()
                  .setWidgets(widgets);
    card.setSections(Collections.singletonList(section));
    
    return card;
  }

}
