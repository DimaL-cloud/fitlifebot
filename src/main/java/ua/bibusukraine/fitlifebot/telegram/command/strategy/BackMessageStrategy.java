package ua.bibusukraine.fitlifebot.telegram.command.strategy;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;

@Component
public class BackMessageStrategy implements TelegramMessageStrategy {

    private final ApplicationEventPublisher applicationEventPublisher;

    public BackMessageStrategy(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void execute(Message message) {
        StartMessageStrategy startMessageStrategy = new StartMessageStrategy(applicationEventPublisher);
        startMessageStrategy.execute(message);
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.BACK;
    }

}
