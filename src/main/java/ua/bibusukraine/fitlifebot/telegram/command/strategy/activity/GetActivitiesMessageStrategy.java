package ua.bibusukraine.fitlifebot.telegram.command.strategy.activity;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.annotation.PostConstruct;
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
import ua.bibusukraine.fitlifebot.telegram.BotInitializer;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;
import ua.bibusukraine.fitlifebot.util.TelegramMessageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class GetActivitiesMessageStrategy implements TelegramMessageStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(BotInitializer.class);
    private static final int COLUMN_AMOUNT = 5;
    private static final float[] COLUMN_WIDTHS = {1f, 2f, 2f, 2f, 3f};
    private static final String FONT_PATH = "fonts/Ubuntu-Regular.ttf";
    private static final String FILE_NAME = "activities.pdf";
    private static final String TITLE = "Звіт по активностям";
    private static final String ID_COLUMN_NAME = "Айді";
    private static final String NAME_COLUMN_NAME = "Назва";
    private static final String BURNED_CALORIES_COLUMN_NAME = "Кількість спалених калорій";
    private static final String SPENT_TIME_COLUMN_NAME = "Витрачений час (хв)";
    private static final String NOTES_COLUMN_NAME = "Замітки";
    private static final String CREATE_FILE_ERROR_MESSAGE = "Помилка при створенні файлу";

    private final ActivityRepository activityRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    private Font font;

    public GetActivitiesMessageStrategy(ActivityRepository activityRepository,
                                        ApplicationEventPublisher applicationEventPublisher) {
        this.activityRepository = activityRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostConstruct
    public void init() throws DocumentException, IOException {
        BaseFont bf = BaseFont.createFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        font = new Font(bf);
    }

    @Override
    public void execute(Message message) {
        String filePath = FILE_NAME;
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            Paragraph title = new Paragraph(TITLE, font);
            title.setSpacingAfter(20);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            PdfPTable table = createTable();
            List<Activity> activities = activityRepository.findActivitiesByChatId(message.getChatId());
            activities.forEach(activity -> addActivityToTable(table, activity));
            document.add(table);
            document.close();
        } catch (DocumentException | IOException e) {
            LOGGER.error("Error while creating PDF file: " + e.getMessage());
            SendMessage response = TelegramMessageUtil.buildSendMessage(message.getChatId(), CREATE_FILE_ERROR_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        }
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(message.getChatId().toString());
        sendDocument.setDocument(new InputFile(new File(filePath)));
        applicationEventPublisher.publishEvent(sendDocument);
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.GET_ACTIVITIES_REPORT;
    }

    private PdfPTable createTable() throws DocumentException {
        PdfPTable table = new PdfPTable(COLUMN_AMOUNT);
        table.setWidths(COLUMN_WIDTHS);
        addCell(table, ID_COLUMN_NAME);
        addCell(table, NAME_COLUMN_NAME);
        addCell(table, BURNED_CALORIES_COLUMN_NAME);
        addCell(table, SPENT_TIME_COLUMN_NAME);
        addCell(table, NOTES_COLUMN_NAME);
        return table;
    }

    private void addActivityToTable(PdfPTable table, Activity activity) {
        addCell(table, activity.getId().toString());
        addCell(table, activity.getName());
        addCell(table, activity.getBurnedCalories().toString());
        addCell(table, activity.getSpentTimeInMinutes().toString());
        addCell(table, activity.getNotes());
    }

    private void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

}