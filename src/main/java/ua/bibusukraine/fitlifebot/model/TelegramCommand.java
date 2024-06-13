package ua.bibusukraine.fitlifebot.model;

import java.util.HashMap;
import java.util.Map;

public enum TelegramCommand {
    START("/start"),
    ACTIVITIES("Activities \uD83C\uDFC0"),
    NUTRITION("Nutrition \uD83C\uDF49"),
    WEIGHT("Weight ⚖️"),
    SLEEP("Sleep \uD83D\uDE34"),
    ADD_ACTIVITY("Add activity"),
    ADD_SLEEP("Add sleep"),
    ADD_WEIGHT("Add weight"),
    REMOVE_ACTIVITY("Remove activity"),
    REMOVE_SLEEP("Remove sleep"),
    REMOVE_WEIGHT("Remove weight"),
    GET_ACTIVITIES_REPORT("Get activities report"),
    GET_SLEEP_REPORT("Get sleep report"),
    GET_WEIGHT_REPORT("Get weight report"),
    BACK("Back"),
    UNRECOGNISED("Unrecognised"),
    CALCULATORS("Calculators \uD83D\uDCCA"),
    BMI("Body Mass Index \uD83E\uDDCD\u200D♀️"),
    BMR("Basal Metabolic Rate \uD83C\uDFCB️\u200D♂️");

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
