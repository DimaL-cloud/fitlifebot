package ua.bibusukraine.fitlifebot.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
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
                SendMessage response = telegramMessageStrategyContext.getStrategy(message).buildSendMessage();
                sendMessage(response);
            }
        }
    }

    private void sendMessage(SendMessage response) {
        try {
            execute(response);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while sending message", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

}
