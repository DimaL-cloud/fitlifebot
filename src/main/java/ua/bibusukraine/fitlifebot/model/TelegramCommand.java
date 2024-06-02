package ua.bibusukraine.fitlifebot.model;

import java.util.HashMap;
import java.util.Map;

public enum TelegramCommand {
    START("/start"),
    ACTIVITIES("Активності \uD83C\uDFC0"),
    NUTRITION("Харчування \uD83C\uDF49"),
    WEIGHT("Вага ⚖\uFE0F"),
    SLEEP("Сон \uD83D\uDE34"),
    ADD_ACTIVITY("Додати активність"),
    REMOVE_ACTIVITY("Видалити активність"),
    GET_ACTIVITIES_REPORT("Отримати звіт по активностям"),
    BACK("Назад"),
    UNRECOGNISED("Не впізнано");

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
