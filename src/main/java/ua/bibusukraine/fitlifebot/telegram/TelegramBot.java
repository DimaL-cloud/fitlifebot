package ua.bibusukraine.fitlifebot.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.bibusukraine.fitlifebot.telegram.command.TelegramMessageStrategyContext;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBot.class);

    private final TelegramMessageStrategyContext telegramMessageStrategyContext;

    @Value("${telegram.bot-name}")
    private String botUsername;

    public TelegramBot(@Value("${telegram.bot-token}") String botToken,
                       TelegramMessageStrategyContext telegramMessageStrategyContext) {
        super(botToken);
        this.telegramMessageStrategyContext = telegramMessageStrategyContext;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            LOGGER.info("Received message: {}", update.getMessage().getText());
            Message message = update.getMessage();
            if (message != null) {
                telegramMessageStrategyContext.getStrategy(message).execute(message);
            }
        }
    }

    @EventListener
    public void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while sending message", e);
        }
    }

    @EventListener
    public void sendDocument(SendDocument sendDocument) {
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while sending message", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

}
