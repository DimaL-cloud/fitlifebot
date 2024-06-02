package ua.bibusukraine.fitlifebot.telegram.command.strategy.activity;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

public class GetActivitiesMessageStrategy extends TelegramMessageStrategy {

    private final Message message;

    public GetActivitiesMessageStrategy(Message message) {
        this.message = message;
    }

    @Override
    public SendMessage buildSendMessage() {
        SendMessage response = new SendMessage();

        return response;
    }

}
