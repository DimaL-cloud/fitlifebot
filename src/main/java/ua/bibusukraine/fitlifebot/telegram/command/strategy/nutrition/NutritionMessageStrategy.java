package ua.bibusukraine.fitlifebot.telegram.command.strategy.nutrition;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

import java.util.ArrayList;
import java.util.List;

@Component
public class NutritionMessageStrategy implements TelegramMessageStrategy {
    private static final String CHOOSE_CATEGORY_TEXT = "У цьому розділі ви можете керувати харчуванням. Оберіть дію";

    private final ApplicationEventPublisher applicationEventPublisher;

    public NutritionMessageStrategy(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void execute(Message message) {
        SendMessage response = new SendMessage(String.valueOf(message.getChatId()), CHOOSE_CATEGORY_TEXT);
        response.setReplyMarkup(getReplyKeyboardMarkup());
        applicationEventPublisher.publishEvent(response);
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.NUTRITION;
    }

    protected ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(getKeyboardRow(TelegramCommand.ADD_DISH));
        keyboard.add(getKeyboardRow(TelegramCommand.REMOVE_DISH));
        keyboard.add(getKeyboardRow(TelegramCommand.GET_DISH_REPORT));
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
