package ua.bibusukraine.fitlifebot.telegram.command.strategy.nutrition;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

@Component
public class NutritionMessageStrategy implements TelegramMessageStrategy {

    private final ApplicationEventPublisher applicationEventPublisher;

    public NutritionMessageStrategy(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void execute(Message message) {
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.NUTRITION;
    }

}
