package ua.bibusukraine.fitlifebot.telegram.command.strategy.activity;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.model.Activity;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.repository.ActivityRepository;
import ua.bibusukraine.fitlifebot.telegram.command.ActivityHolder;
import ua.bibusukraine.fitlifebot.telegram.command.CommandHolder;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

import java.util.Optional;

public class AddActivityMessageStrategy extends TelegramMessageStrategy {

    private static final String ENTER_ACTIVITY_NAME_MESSAGE = "Введіть назву активності";
    private static final String ENTER_BURNED_CALORIES_MESSAGE = "Введіть кількість спалених калорій";
    private static final String ENTER_SPENT_TIME_MESSAGE = "Введіть кількість витраченого часу в хвилинах";
    private static final String ENTER_NOTES_MESSAGE = "Введіть замітки";
    private static final String ACTIVITY_ADDED_MESSAGE = "Активність успішно додана";
    private static final String INCORRECT_INPUT_FORMAT_MESSAGE = "Неправильний формат введення";
    private static final String ACTIVITY_ALREADY_ADDED = "Активність вже додана.";

    private final Message message;
    private final CommandHolder commandHolder;
    private final ActivityHolder activityHolder;
    private final ActivityRepository activityRepository;

    public AddActivityMessageStrategy(Message message,
                                      CommandHolder commandHolder,
                                      ActivityHolder activityHolder, ActivityRepository activityRepository) {
        this.message = message;
        this.commandHolder = commandHolder;
        this.activityHolder = activityHolder;
        this.activityRepository = activityRepository;
    }

    @Override
    public SendMessage buildSendMessage() {
        Activity activity = activityHolder.getActivity(message.getChatId());
        if (activity == null) {
            activityHolder.putActivity(message.getChatId(), new Activity());
            return buildRequestFieldMessage(message.getChatId(), ENTER_ACTIVITY_NAME_MESSAGE);
        }
        if (activity.getName() == null) {
            return handleNameField(activity);
        } else if (activity.getBurnedCalories() == null) {
            return handleBurnedCaloriesField(activity);
        } else if (activity.getSpentTimeInMinutes() == null) {
            return handleSpentTimeInMinutesField(activity);
        } else if (activity.getNotes() == null) {
            return handleNotesField(activity);
        }
        return buildRequestFieldMessage(message.getChatId(), ACTIVITY_ALREADY_ADDED);
    }

    private SendMessage handleNameField(Activity activity) {
        activity.setName(message.getText());
        return buildRequestFieldMessage(message.getChatId(), ENTER_BURNED_CALORIES_MESSAGE);
    }

    private SendMessage handleBurnedCaloriesField(Activity activity) {
        String input = message.getText();
        Optional<Double> optionalCalories = parseCalories(input);
        if (optionalCalories.isEmpty() || optionalCalories.get() <= 0) {
            return buildRequestFieldMessage(message.getChatId(), INCORRECT_INPUT_FORMAT_MESSAGE);
        }
        activity.setBurnedCalories(optionalCalories.get());
        return buildRequestFieldMessage(message.getChatId(), ENTER_SPENT_TIME_MESSAGE);
    }

    private Optional<Double> parseCalories(String input) {
        try {
            return Optional.of(Double.parseDouble(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private SendMessage handleSpentTimeInMinutesField(Activity activity) {
        String input = message.getText();
        Optional<Integer> optionalMinutes = parseMinutes(input);
        if (optionalMinutes.isEmpty() || optionalMinutes.get() <= 0) {
            return buildRequestFieldMessage(message.getChatId(), INCORRECT_INPUT_FORMAT_MESSAGE);
        }
        activity.setSpentTimeInMinutes(optionalMinutes.get());
        return buildRequestFieldMessage(message.getChatId(), ENTER_NOTES_MESSAGE);
    }

    private Optional<Integer> parseMinutes(String input) {
        try {
            return Optional.of(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private SendMessage handleNotesField(Activity activity) {
        activity.setNotes(message.getText());
        activityRepository.save(activity);
        activityHolder.removeActivity(message.getChatId());
        commandHolder.putLastUserCommand(message.getChatId(), TelegramCommand.ACTIVITIES);
        SendMessage response = buildRequestFieldMessage(message.getChatId(), ACTIVITY_ADDED_MESSAGE);
        ActivitiesMessageStrategy activitiesMessageStrategy = new ActivitiesMessageStrategy(message);
        response.setReplyMarkup(activitiesMessageStrategy.buildSendMessage().getReplyMarkup());
        return response;
    }

}