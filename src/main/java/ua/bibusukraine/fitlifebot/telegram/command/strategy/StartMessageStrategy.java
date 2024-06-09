package ua.bibusukraine.fitlifebot.telegram.command.strategy;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartMessageStrategy implements TelegramMessageStrategy {

    private static final String CHOOSE_CATEGORY_TEXT = "Choose a category";

    private final ApplicationEventPublisher applicationEventPublisher;

    public StartMessageStrategy(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void execute(Message message) {
        SendMessage response = new SendMessage(message.getChatId().toString(), CHOOSE_CATEGORY_TEXT);
        ReplyKeyboardMarkup keyboardMarkup = getReplyKeyboardMarkup();
        response.setReplyMarkup(keyboardMarkup);
        applicationEventPublisher.publishEvent(response);
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.START;
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(getKeyboardRow(TelegramCommand.ACTIVITIES));
        keyboard.add(getKeyboardRow(TelegramCommand.NUTRITION));
        keyboard.add(getKeyboardRow(TelegramCommand.WEIGHT));
        keyboard.add(getKeyboardRow(TelegramCommand.SLEEP));
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private KeyboardRow getKeyboardRow(TelegramCommand command) {
        KeyboardRow row = new KeyboardRow();
        row.add(command.getText());
        return row;
    }

}
