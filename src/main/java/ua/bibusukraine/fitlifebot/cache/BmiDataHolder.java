package ua.bibusukraine.fitlifebot.cache;

import org.springframework.stereotype.Component;
import ua.bibusukraine.fitlifebot.model.BmiData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BmiDataHolder {

    private final Map<Long, BmiData> chatIdToBmiDataMap = new ConcurrentHashMap<>();

    public BmiData getBmiData(Long chatId) {
        return chatIdToBmiDataMap.get(chatId);
    }

    public void putBmiData(Long chatId, BmiData bmiData) {
        chatIdToBmiDataMap.put(chatId, bmiData);
    }

    public void removeBmiData(Long chatId) {
        chatIdToBmiDataMap.remove(chatId);
    }

}
