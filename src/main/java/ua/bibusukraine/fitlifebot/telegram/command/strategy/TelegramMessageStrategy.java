package ua.bibusukraine.fitlifebot.telegram.command.strategy;

import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;

public interface TelegramMessageStrategy {

    void execute(Message message);

    TelegramCommand getCommand();

}
