package ua.bibusukraine.fitlifebot.telegram.command.strategy;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.util.TelegramMessageUtil;

@Component
public class UnrecognisedMessageStrategy implements TelegramMessageStrategy {

    private static final String UNRECOGNISED_MESSAGE = "Нерозпізнана команда";

    private final ApplicationEventPublisher applicationEventPublisher;

    public UnrecognisedMessageStrategy(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void execute(Message message) {
        SendMessage sendMessage = TelegramMessageUtil.buildSendMessage(message.getChatId(), UNRECOGNISED_MESSAGE);
        applicationEventPublisher.publishEvent(sendMessage);
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.UNRECOGNISED;
    }

}
