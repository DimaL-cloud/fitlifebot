package ua.bibusukraine.fitlifebot.telegram.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.cache.CommandHolder;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TelegramMessageStrategyContext {

    private final CommandHolder commandHolder;
    private final Map<TelegramCommand, TelegramMessageStrategy> strategyMap;

    public TelegramMessageStrategyContext(List<TelegramMessageStrategy> strategies,
                                          CommandHolder commandHolder) {
        strategyMap = strategies.stream()
                .collect(Collectors.toMap(TelegramMessageStrategy::getCommand, strategy -> strategy));
        this.commandHolder = commandHolder;
    }

    public TelegramMessageStrategy getStrategy(Message message) {
        TelegramCommand command = TelegramCommand.getTelegramCommandByText(message.getText());
        TelegramCommand lastUserCommand = commandHolder.getLastUserCommand(message.getChatId());
        command = resolveUnrecognisedCommand(command, lastUserCommand);
        updateLastUserCommand(command, message.getChatId());
        return strategyMap.get(command);
    }

    private TelegramCommand resolveUnrecognisedCommand(TelegramCommand command, TelegramCommand lastUserCommand) {
        if (command == TelegramCommand.UNRECOGNISED && lastUserCommand != null) {
            command = switch (lastUserCommand) {
                case ADD_ACTIVITY -> TelegramCommand.ADD_ACTIVITY;
                case REMOVE_ACTIVITY -> TelegramCommand.REMOVE_ACTIVITY;
                default -> command;
            };
        }
        return command;
    }

    private void updateLastUserCommand(TelegramCommand command, Long chatId) {
        if (command != TelegramCommand.UNRECOGNISED) {
            commandHolder.putLastUserCommand(chatId, command);
        }
    }

}
