package ua.bibusukraine.fitlifebot.cache;

import org.springframework.stereotype.Component;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommandHolder {
    
    private final Map<Long, TelegramCommand> lastUserCommand = new ConcurrentHashMap<>();

    public TelegramCommand getLastUserCommand(Long chatId) {
        return lastUserCommand.get(chatId);
    }

    public void putLastUserCommand(Long chatId, TelegramCommand command) {
        lastUserCommand.put(chatId, command);
    }

    public void removeLastUserCommand(Long chatId) {
        lastUserCommand.remove(chatId);
    }

}
