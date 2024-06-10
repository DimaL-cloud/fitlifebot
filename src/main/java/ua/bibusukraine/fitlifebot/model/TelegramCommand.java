package ua.bibusukraine.fitlifebot.model;

import java.util.HashMap;
import java.util.Map;

public enum TelegramCommand {
    START("/start"),
    ACTIVITIES("Активності \uD83C\uDFC0"),
    NUTRITION("Харчування \uD83C\uDF49"),
    WEIGHT("Вага ⚖️"),
    SLEEP("Сон \uD83D\uDE34"),
    ADD_ACTIVITY("Додати активність"),
    ADD_SLEEP("Додати сон"),
    ADD_WEIGHT("Додати вагу"),
    REMOVE_ACTIVITY("Видалити активність"),
    REMOVE_SLEEP("Видалити сон"),
    REMOVE_WEIGHT("Видалити вагу"),
    GET_ACTIVITIES_REPORT("Отримати звіт по активностям"),
    GET_SLEEP_REPORT("Отримати звіт по записам сну"),
    GET_WEIGHT_REPORT("Отримати звіт по записам ваги"),
    BACK("Назад"),
    UNRECOGNISED("Не впізнано"),
    ADD_DISH("Додати страву"),
    REMOVE_DISH("Видалити страву"),
    GET_DISH_REPORT("Отримати звіт по їжі");

    private final String text;

    private static final Map<String, TelegramCommand> lookup = new HashMap<>();

    static {
        for (TelegramCommand command : TelegramCommand.values()) {
            lookup.put(command.getText(), command);
        }
    }

    TelegramCommand(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static TelegramCommand getTelegramCommandByText(String text) {
        TelegramCommand telegramCommand = lookup.get(text);
        return telegramCommand != null ? telegramCommand : UNRECOGNISED;
    }
}
