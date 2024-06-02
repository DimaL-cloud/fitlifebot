package ua.bibusukraine.fitlifebot.telegram.command.strategy.activity;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.repository.ActivityRepository;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

@Component
public class RemoveActivityMessageStrategy implements TelegramMessageStrategy {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ActivityRepository activityRepository;

    public RemoveActivityMessageStrategy(ApplicationEventPublisher applicationEventPublisher, ActivityRepository activityRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.activityRepository = activityRepository;
    }

    @Override
    public void execute(Message message) {
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.REMOVE_ACTIVITY;
    }

}
