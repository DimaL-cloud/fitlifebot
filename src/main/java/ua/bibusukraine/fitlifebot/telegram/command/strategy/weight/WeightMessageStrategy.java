package ua.bibusukraine.fitlifebot.telegram.command.strategy.weight;

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
public class WeightMessageStrategy implements TelegramMessageStrategy {

    private static final String CHOOSE_SLEEP_ACTION_TEXT = "В цьому розділі ви можете керувати записами вашої ваги. Оберіть бажану дію:";

    private final ApplicationEventPublisher applicationEventPublisher;

    public WeightMessageStrategy(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void execute(Message message) {
        SendMessage response = new SendMessage(message.getChatId().toString(), CHOOSE_SLEEP_ACTION_TEXT);
        ReplyKeyboardMarkup keyboardMarkup = getReplyKeyboardMarkup();
        response.setReplyMarkup(keyboardMarkup);
        applicationEventPublisher.publishEvent(response);
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.WEIGHT;
    }

    protected ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(getKeyboardRow(TelegramCommand.ADD_WEIGHT));
        keyboard.add(getKeyboardRow(TelegramCommand.REMOVE_WEIGHT));
        keyboard.add(getKeyboardRow(TelegramCommand.GET_WEIGHT_REPORT));
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
