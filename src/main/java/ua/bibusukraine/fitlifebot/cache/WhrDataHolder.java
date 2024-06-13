package ua.bibusukraine.fitlifebot.cache;

import org.springframework.stereotype.Component;
import ua.bibusukraine.fitlifebot.model.WhrData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WhrDataHolder {

    private final Map<Long, WhrData> chatIdToWhrDataMap = new ConcurrentHashMap<>();

    public WhrData getWhrData(Long chatId) {
        return chatIdToWhrDataMap.get(chatId);
    }

    public void putWhrData(Long chatId, WhrData whrData) {
        chatIdToWhrDataMap.put(chatId, whrData);
    }

    public void removeWhrData(Long chatId) {
        chatIdToWhrDataMap.remove(chatId);
    }

}
