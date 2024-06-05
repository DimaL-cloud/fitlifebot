package ua.bibusukraine.fitlifebot.telegram.command.strategy.weight;

import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.model.ReportState;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.model.Weight;
import ua.bibusukraine.fitlifebot.repository.WeightRepository;
import ua.bibusukraine.fitlifebot.services.ReportService;
import ua.bibusukraine.fitlifebot.telegram.BotInitializer;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;
import ua.bibusukraine.fitlifebot.util.TelegramMessageUtil;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GetWeightMessageStrategy implements TelegramMessageStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(BotInitializer.class);
    private static final String CREATE_FILE_ERROR_MESSAGE = "Error creating the file";
    private static final String ENTER_START_DATE_MESSAGE = "Please enter the start date in the format dd.MM.yyyy";
    private static final String ENTER_END_DATE_MESSAGE = "Please enter the end date in the format dd.MM.yyyy";
    private static final String REPORT_GENERATING_MESSAGE = "Generating the report...";
    private static final String INCORRECT_DATE_INPUT_FORMAT_MESSAGE = "Incorrect date format. Please enter the date in the format dd.MM.yyyy";

    private final WeightRepository weightRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ReportService reportService;
    private final Map<Long, ReportState> stateMap = new HashMap<>();
    private final Map<Long, LocalDate> startDateMap = new HashMap<>();

    public GetWeightMessageStrategy(WeightRepository weightRepository,
                                    ApplicationEventPublisher applicationEventPublisher,
                                    ReportService reportService) {
        this.weightRepository = weightRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.reportService = reportService;
    }

    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();
        ReportState state = stateMap.getOrDefault(chatId, ReportState.START);
        LOGGER.info("Chat ID: {}, Current state: {}", chatId, state);

        switch (state) {
            case START:
                stateMap.put(chatId, ReportState.WAITING_FOR_START_DATE);
                sendMessage(chatId, ENTER_START_DATE_MESSAGE);
                break;

            case WAITING_FOR_START_DATE:
                LocalDate startDate = parseDate(message.getText());
                if (startDate == null) {
                    sendMessage(chatId, INCORRECT_DATE_INPUT_FORMAT_MESSAGE);
                } else {
                    startDateMap.put(chatId, startDate);
                    stateMap.put(chatId, ReportState.WAITING_FOR_END_DATE);
                    sendMessage(chatId, ENTER_END_DATE_MESSAGE);
                }
                break;

            case WAITING_FOR_END_DATE:
                LocalDate endDate = parseDate(message.getText());
                if (endDate == null) {
                    sendMessage(chatId, INCORRECT_DATE_INPUT_FORMAT_MESSAGE);
                } else {
                    LocalDate startDateFromMap = startDateMap.remove(chatId);
                    stateMap.remove(chatId);
                    sendMessage(chatId, REPORT_GENERATING_MESSAGE);
                    generateReport(chatId, startDateFromMap, endDate);
                }
                break;
        }
    }

    private LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (DateTimeParseException e) {
            LOGGER.error("Date parsing error: {}", e.getMessage());
            return null;
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage response = TelegramMessageUtil.buildSendMessage(chatId, text);
        applicationEventPublisher.publishEvent(response);
    }

    private void generateReport(Long chatId, LocalDate startDate, LocalDate endDate) {
        try {
            List<Weight> weights = weightRepository.findWeightsByChatIdAndCreatedAtBetween(chatId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
            File resultPdf = reportService.getReport(weights, Weight.class);
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(chatId.toString());
            sendDocument.setDocument(new InputFile(resultPdf));
            applicationEventPublisher.publishEvent(sendDocument);
        } catch (DocumentException | IOException | RuntimeException e) {
            LOGGER.error("Error while creating PDF file: {}", e.getMessage());
            sendMessage(chatId, CREATE_FILE_ERROR_MESSAGE);
        }
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.GET_WEIGHT_REPORT;
    }

}
