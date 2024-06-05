package ua.bibusukraine.fitlifebot.cache;

import org.springframework.stereotype.Component;
import ua.bibusukraine.fitlifebot.model.Sleep;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SleepHolder {
    private final Map<Long, Sleep> chatIdToSleepMap = new ConcurrentHashMap<>();

    public Sleep getSleep(Long chatId) {
        return chatIdToSleepMap.get(chatId);
    }

    public void putSleep(Long chatId, Sleep sleep) {
        chatIdToSleepMap.put(chatId, sleep);
    }

    public void removeSleep(Long chatId) {
        chatIdToSleepMap.remove(chatId);
    }
}
