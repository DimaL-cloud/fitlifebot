package ua.bibusukraine.fitlifebot.model;

import java.util.HashMap;
import java.util.Map;

public enum TelegramCommand {
    START("/start", false),
    ACTIVITIES("Activities \uD83C\uDFC0", false),
    NUTRITION("Nutrition \uD83C\uDF49", false),
    WEIGHT("Weight ⚖️", false),
    SLEEP("Sleep \uD83D\uDE34", false),
    ADD_ACTIVITY("Add activity", true),
    ADD_SLEEP("Add sleep", true),
    ADD_WEIGHT("Add weight", true),
    REMOVE_ACTIVITY("Remove activity", true),
    REMOVE_SLEEP("Remove sleep", true),
    REMOVE_WEIGHT("Remove weight", true),
    GET_ACTIVITIES_REPORT("Get activities report", false),
    GET_SLEEP_REPORT("Get sleep report", false),
    GET_WEIGHT_REPORT("Get weight report", true),
    BACK("Back", false),
    UNRECOGNISED("Unrecognised", false),
    CALCULATORS("Calculators \uD83D\uDCCA", false),
    BMI("Body Mass Index \uD83E\uDDCD\u200D♀️", true),
    BMR("Basal Metabolic Rate \uD83C\uDFCB️\u200D♂️", true),
    WHR("Waist-to-Hip Ratio \uD83E\uDD38\u200D♂️", true),
    ADD_DISH("Add dish", true),
    REMOVE_DISH("Remove dish", true),
    GET_DISH_REPORT("Get dish report", false);

    private final String text;
    private final boolean requestsData;

    private static final Map<String, TelegramCommand> lookup = new HashMap<>();

    static {
        for (TelegramCommand command : TelegramCommand.values()) {
            lookup.put(command.getText(), command);
        }
    }

    TelegramCommand(String text, boolean requestsData) {
        this.text = text;
        this.requestsData = requestsData;
    }

    public String getText() {
        return text;
    }

    public boolean requestsData() {
        return requestsData;
    }

    public static TelegramCommand getTelegramCommandByText(String text) {
        TelegramCommand telegramCommand = lookup.get(text);
        return telegramCommand != null ? telegramCommand : UNRECOGNISED;
    }
}