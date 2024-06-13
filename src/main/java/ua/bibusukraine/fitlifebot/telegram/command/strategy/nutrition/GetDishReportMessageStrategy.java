package ua.bibusukraine.fitlifebot.telegram.command.strategy.nutrition;

import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.model.Product;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.service.ProductService;
import ua.bibusukraine.fitlifebot.service.ReportService;

import java.io.File;
import java.io.FileNotFoundException;

@Component
public class GetDishReportMessageStrategy extends NutritionMessageStrategy {
  private static final Logger LOG = LoggerFactory.getLogger(GetDishReportMessageStrategy.class);
  private static final String FILE_CREATION_ERROR = "Error happened on file creation, try again";
  private final ReportService reportService;
  private final ProductService productService;
  private final ApplicationEventPublisher applicationEventPublisher;

  public GetDishReportMessageStrategy(ReportService reportService, ProductService productService, ApplicationEventPublisher applicationEventPublisher) {
    super(applicationEventPublisher);
    this.reportService = reportService;
    this.productService = productService;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public void execute(Message message) {
    try {
      File resultPdf = reportService.getReport(productService.findProductsByChatId(message.getChatId()), Product.class);
      SendDocument sendDocument = new SendDocument();
      sendDocument.setChatId(message.getChatId().toString());
      sendDocument.setDocument(new InputFile(resultPdf));
      applicationEventPublisher.publishEvent(sendDocument);
    } catch (DocumentException | FileNotFoundException e) {
      LOG.error("Error while creating product PDF file:", e);
      SendMessage response = new SendMessage(String.valueOf(message.getChatId()), FILE_CREATION_ERROR);
      response.setReplyMarkup(getReplyKeyboardMarkup());
      applicationEventPublisher.publishEvent(response);
    }
  }

  @Override
  public TelegramCommand getCommand() {
    return TelegramCommand.GET_DISH_REPORT;
  }
}
