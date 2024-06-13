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
import ua.bibusukraine.fitlifebot.services.ProductService;

@Component
public class RemoveDishMessageStrategy extends NutritionMessageStrategy {
  private static final Logger LOG = LoggerFactory.getLogger(RemoveDishMessageStrategy.class);
  private static final String REQUEST_PRODUCT_ID = "Please enter the dish/product id for removal";
  private static final String INCORRECT_INPUT_FORMAT_MESSAGE = "Your message has the wrong format";
  private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found in database";
  private static final String SUCCESSFULLY_DELETED_MESSAGE = "You successfully deleted your product/dish";
  private final ProductService productService;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final ProductHolder productHolder;

  public RemoveDishMessageStrategy(ProductService productService, ApplicationEventPublisher applicationEventPublisher, ProductHolder productHolder) {
    super(applicationEventPublisher);
    this.productService = productService;
    this.applicationEventPublisher = applicationEventPublisher;
    this.productHolder = productHolder;
  }

  @Override
  public void execute(Message message) {
    Product productForRemoval = productHolder.getProductByChatId(message.getChatId());
    if (productForRemoval == null) {
      productForRemoval = new Product();
      productHolder.putProduct(message.getChatId(), productForRemoval);
      SendMessage response = new RequestFieldMessage(String.valueOf(message.getChatId()), REQUEST_PRODUCT_ID);
      applicationEventPublisher.publishEvent(response);
    } else {
      try {
        productService.findById(Long.parseLong(message.getText()))
            .filter(product -> message.getChatId().equals(product.getChatId()))
            .ifPresentOrElse(product -> productService.removeById(product.getId()),
                () -> {
                  throw new RuntimeException("No product with such an id in database");
                });
        SendMessage response = new SendMessage(String.valueOf(message.getChatId()), SUCCESSFULLY_DELETED_MESSAGE);
        response.setReplyMarkup(getReplyKeyboardMarkup());
        applicationEventPublisher.publishEvent(response);
        productHolder.removeProductByChatId(message.getChatId());
      } catch (NumberFormatException e) {
        SendMessage response = new RequestFieldMessage(String.valueOf(message.getChatId()), INCORRECT_INPUT_FORMAT_MESSAGE);
        applicationEventPublisher.publishEvent(response);
      } catch (RuntimeException e) {
        SendMessage response = new SendMessage(String.valueOf(message.getChatId()), PRODUCT_NOT_FOUND_MESSAGE);
        response.setReplyMarkup(getReplyKeyboardMarkup());
        applicationEventPublisher.publishEvent(response);
        productHolder.removeProductByChatId(message.getChatId());
      }
    }
  }

  @Override
  public TelegramCommand getCommand() {
    return TelegramCommand.REMOVE_DISH;
  }
}
