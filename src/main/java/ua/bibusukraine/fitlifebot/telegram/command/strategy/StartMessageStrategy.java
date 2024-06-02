package ua.bibusukraine.fitlifebot.telegram.command.strategy;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;

import java.util.ArrayList;
import java.util.List;

public class StartMessageStrategy extends TelegramMessageStrategy {

    private static final String CHOOSE_CATEGORY_TEXT = "Оберіть категорію";

    private final Message message;

    public StartMessageStrategy(Message message) {
        this.message = message;
    }

    @Override
    public SendMessage buildSendMessage() {
        SendMessage response = new SendMessage();
        response.setChatId(message.getChatId().toString());

        ReplyKeyboardMarkup keyboardMarkup = getReplyKeyboardMarkup();
        response.setReplyMarkup(keyboardMarkup);
        response.setText(CHOOSE_CATEGORY_TEXT);
        return response;
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
