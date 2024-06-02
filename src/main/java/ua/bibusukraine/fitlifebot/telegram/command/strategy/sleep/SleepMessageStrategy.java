package ua.bibusukraine.fitlifebot.telegram.command.strategy.sleep;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

public class SleepMessageStrategy extends TelegramMessageStrategy {

    private final Message message;

    public SleepMessageStrategy(Message message) {
        this.message = message;
    }

    @Override
    public SendMessage buildSendMessage() {
        return null;
    }

}
