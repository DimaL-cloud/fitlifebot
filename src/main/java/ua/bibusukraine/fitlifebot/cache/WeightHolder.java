package ua.bibusukraine.fitlifebot.cache;

import org.springframework.stereotype.Component;
import ua.bibusukraine.fitlifebot.model.Sleep;
import ua.bibusukraine.fitlifebot.model.Weight;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WeightHolder {
    private final Map<Long, Weight> chatIdToSleepMap = new ConcurrentHashMap<>();

    public Weight getWeight(Long chatId) {
        return chatIdToSleepMap.get(chatId);
    }

    public void putWeight(Long chatId, Weight weight) {
        chatIdToSleepMap.put(chatId, weight);
    }

    public void removeWeight(Long chatId) {
        chatIdToSleepMap.remove(chatId);
    }
}
