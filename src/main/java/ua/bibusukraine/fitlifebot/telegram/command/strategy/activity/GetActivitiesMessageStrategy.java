package ua.bibusukraine.fitlifebot.telegram.command.strategy.activity;

import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.model.Activity;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.repository.ActivityRepository;
import ua.bibusukraine.fitlifebot.services.ReportService;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

import java.io.File;
import java.io.IOException;

@Component
public class GetActivitiesMessageStrategy implements TelegramMessageStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetActivitiesMessageStrategy.class);
    private static final String CREATE_FILE_ERROR_MESSAGE = "Error creating the file";
    private final ActivityRepository activityRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ReportService reportService;

    public GetActivitiesMessageStrategy(ActivityRepository activityRepository, ApplicationEventPublisher applicationEventPublisher, ReportService reportService) {
        this.activityRepository = activityRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.reportService = reportService;
    }

    @Override
    public void execute(Message message) {
        try {
            File resultPdf = reportService.getReport(activityRepository.findActivitiesByChatId(message.getChatId()), Activity.class);
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(message.getChatId().toString());
            sendDocument.setDocument(new InputFile(resultPdf));
            applicationEventPublisher.publishEvent(sendDocument);
        } catch (DocumentException | IOException | RuntimeException e) {
            LOGGER.error("Error while creating PDF file: {}", e.getMessage());
            SendMessage response = new SendMessage(message.getChatId().toString(), CREATE_FILE_ERROR_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        }
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.GET_ACTIVITIES_REPORT;
    }

}