package ua.bibusukraine.fitlifebot.telegram.command.strategy.nutrition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.cache.ProductHolder;
import ua.bibusukraine.fitlifebot.model.Product;
import ua.bibusukraine.fitlifebot.model.RequestFieldMessage;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.service.ProductService;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class AddDishMessageStrategy extends NutritionMessageStrategy {
  private static final Logger LOG = LoggerFactory.getLogger(AddDishMessageStrategy.class);
  private static final String ENTER_PRODUCT_NAME = "Enter the product name:";
  private static final String ENTER_PRODUCT_WEIGHT = "Enter the product weight:";
  private static final String ENTER_CONSUMPTION_DATE = "Enter the time and fate of consumption in ISO format:";
  private static final String ENTER_NOTES = "Enter notes about the product";
  private static final String INCORRECT_INPUT_FORMAT = "Incorrect input format";
  private static final String SUCCESSFULLY_ADDED_PRODUCT = "You successfully added Your product";
  private static final String PRODUCT_ALREADY_EXIST = "The product is already added";

  private final ProductService productService;
  private final ProductHolder productHolder;
  private final ApplicationEventPublisher applicationEventPublisher;

  public AddDishMessageStrategy(ProductService productService, ProductHolder productHolder, ApplicationEventPublisher applicationEventPublisher) {
    super(applicationEventPublisher);
    this.productService = productService;
    this.productHolder = productHolder;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public void execute(Message message) {
    SendMessage response;
    Product product = productHolder.getProductByChatId(message.getChatId());
    if (Objects.isNull(product)) {
      product = new Product();
      product.setChatId(message.getChatId());
      productHolder.putProduct(message.getChatId(), product);
      response = new RequestFieldMessage(String.valueOf(message.getChatId()), ENTER_PRODUCT_NAME);
    } else if (Objects.isNull(product.getName())) {
      product.setName(message.getText());
      response = new RequestFieldMessage(String.valueOf(message.getChatId()), ENTER_PRODUCT_WEIGHT);
    } else if (Objects.isNull(product.getWeight())) {
      try {
        product.setWeight(Double.parseDouble(message.getText()));
        response = new RequestFieldMessage(String.valueOf(message.getChatId()), ENTER_CONSUMPTION_DATE);
      } catch (NumberFormatException e) {
        response = new SendMessage(String.valueOf(message.getChatId()), INCORRECT_INPUT_FORMAT);
        response.setReplyMarkup(getReplyKeyboardMarkup());
      }
    } else if (Objects.isNull(product.getConsumptionDateTime())) {
      try {
        product.setConsumptionDateTime(LocalDateTime.parse(message.getText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response = new RequestFieldMessage(String.valueOf(message.getChatId()), ENTER_NOTES);
      } catch (DateTimeException e) {
        response = new SendMessage(String.valueOf(message.getChatId()), INCORRECT_INPUT_FORMAT);
        response.setReplyMarkup(getReplyKeyboardMarkup());
      }
    } else if (Objects.isNull(product.getNotes())) {
      product.setNotes(message.getText());
      productService.fillProductComplexFields(product);
      productService.saveProduct(product);
      productHolder.removeProductByChatId(message.getChatId());
      response = new SendMessage(String.valueOf(message.getChatId()), SUCCESSFULLY_ADDED_PRODUCT);
      response.setReplyMarkup(getReplyKeyboardMarkup());
      applicationEventPublisher.publishEvent(response);
    } else {
      response = new SendMessage(String.valueOf(message.getChatId()), PRODUCT_ALREADY_EXIST);
      productHolder.removeProductByChatId(message.getChatId());
      response.setReplyMarkup(getReplyKeyboardMarkup());
      applicationEventPublisher.publishEvent(response);
    }
    applicationEventPublisher.publishEvent(response);
  }

  @Override
  public TelegramCommand getCommand() {
    return TelegramCommand.ADD_DISH;
  }
}
