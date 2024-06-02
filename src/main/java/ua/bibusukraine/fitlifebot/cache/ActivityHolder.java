package ua.bibusukraine.fitlifebot.cache;

import org.springframework.stereotype.Component;
import ua.bibusukraine.fitlifebot.model.Activity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ActivityHolder {

    private final Map<Long, Activity> chatIdToActivityMap = new ConcurrentHashMap<>();

    public Activity getActivity(Long chatId) {
        return chatIdToActivityMap.get(chatId);
    }

    public void putActivity(Long chatId, Activity activity) {
        chatIdToActivityMap.put(chatId, activity);
    }

    public void removeActivity(Long chatId) {
        chatIdToActivityMap.remove(chatId);
    }

}
