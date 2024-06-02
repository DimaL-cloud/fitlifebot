package ua.bibusukraine.fitlifebot.telegram.command.strategy.activity;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

import java.util.ArrayList;
import java.util.List;

public class ActivitiesMessageStrategy extends TelegramMessageStrategy {

    private static final String CHOOSE_CATEGORY_TEXT = "У цьому розділі ви можете керувати активностями. Оберіть дію";

    private final Message message;

    public ActivitiesMessageStrategy(Message message) {
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
        keyboard.add(getKeyboardRow(TelegramCommand.ADD_ACTIVITY));
        keyboard.add(getKeyboardRow(TelegramCommand.REMOVE_ACTIVITY));
        keyboard.add(getKeyboardRow(TelegramCommand.GET_ACTIVITIES_REPORT));
        keyboard.add(getKeyboardRow(TelegramCommand.BACK));
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private KeyboardRow getKeyboardRow(TelegramCommand command) {
        KeyboardRow row = new KeyboardRow();
        row.add(command.getText());
        return row;
    }

}
