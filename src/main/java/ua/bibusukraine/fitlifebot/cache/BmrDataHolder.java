package ua.bibusukraine.fitlifebot.cache;

import org.springframework.stereotype.Component;
import ua.bibusukraine.fitlifebot.model.BmrData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BmrDataHolder {

    private final Map<Long, BmrData> chatIdToBmrDataMap = new ConcurrentHashMap<>();

    public BmrData getBmrData(Long chatId) {
        return chatIdToBmrDataMap.get(chatId);
    }

    public void putBmrData(Long chatId, BmrData bmrData) {
        chatIdToBmrDataMap.put(chatId, bmrData);
    }

    public void removeBmrData(Long chatId) {
        chatIdToBmrDataMap.remove(chatId);
    }

}
