package ua.bibusukraine.fitlifebot.telegram.command.strategy.nutrition;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

public class NutritionMessageStrategy extends TelegramMessageStrategy {

    private final Message message;

    public NutritionMessageStrategy(Message message) {
        this.message = message;
    }

    @Override
    public SendMessage buildSendMessage() {
        return null;
    }

}
