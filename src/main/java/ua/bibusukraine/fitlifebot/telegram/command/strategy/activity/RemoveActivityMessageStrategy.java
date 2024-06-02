package ua.bibusukraine.fitlifebot.telegram.command.strategy.activity;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.repository.ActivityRepository;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

public class RemoveActivityMessageStrategy extends TelegramMessageStrategy {

    private final Message message;
    private final ActivityRepository activityRepository;

    public RemoveActivityMessageStrategy(Message message, ActivityRepository activityRepository) {
        this.message = message;
        this.activityRepository = activityRepository;
    }

    @Override
    public SendMessage buildSendMessage() {
        SendMessage response = new SendMessage();

        return response;
    }

}
