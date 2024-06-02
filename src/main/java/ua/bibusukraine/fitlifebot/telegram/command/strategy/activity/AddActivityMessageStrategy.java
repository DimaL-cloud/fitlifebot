package ua.bibusukraine.fitlifebot.telegram.command.strategy.activity;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.util.TelegramMessageUtil;
import ua.bibusukraine.fitlifebot.model.Activity;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.repository.ActivityRepository;
import ua.bibusukraine.fitlifebot.cache.ActivityHolder;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

import java.util.Optional;

@Component
public class AddActivityMessageStrategy implements TelegramMessageStrategy {

    private static final String ENTER_ACTIVITY_NAME_MESSAGE = "Введіть назву активності";
    private static final String ENTER_BURNED_CALORIES_MESSAGE = "Введіть кількість спалених калорій";
    private static final String ENTER_SPENT_TIME_MESSAGE = "Введіть кількість витраченого часу в хвилинах";
    private static final String ENTER_NOTES_MESSAGE = "Введіть замітки";
    private static final String ACTIVITY_ADDED_MESSAGE = "Активність успішно додана";
    private static final String INCORRECT_INPUT_FORMAT_MESSAGE = "Неправильний формат введення";
    private static final String ACTIVITY_ALREADY_ADDED = "Активність вже додана.";

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ActivityHolder activityHolder;
    private final ActivityRepository activityRepository;

    public AddActivityMessageStrategy(ApplicationEventPublisher applicationEventPublisher,
                                      ActivityHolder activityHolder,
                                      ActivityRepository activityRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.activityHolder = activityHolder;
        this.activityRepository = activityRepository;
    }

    @Override
    public void execute(Message message) {
        SendMessage response;
        Activity activity = activityHolder.getActivity(message.getChatId());
        if (activity == null) {
            activity = new Activity();
            activity.setChatId(message.getChatId());
            activityHolder.putActivity(message.getChatId(), activity);
            response = TelegramMessageUtil.buildRequestFieldMessage(message.getChatId(), ENTER_ACTIVITY_NAME_MESSAGE);
        } else if (activity.getName() == null) {
            response = handleNameField(activity, message);
        } else if (activity.getBurnedCalories() == null) {
            response = handleBurnedCaloriesField(activity, message);
        } else if (activity.getSpentTimeInMinutes() == null) {
            response = handleSpentTimeInMinutesField(activity, message);
        } else if (activity.getNotes() == null) {
            response = handleNotesField(activity, message);
        } else response = TelegramMessageUtil.buildRequestFieldMessage(message.getChatId(), ACTIVITY_ALREADY_ADDED);
        applicationEventPublisher.publishEvent(response);
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.ADD_ACTIVITY;
    }

    private SendMessage handleNameField(Activity activity, Message message) {
        activity.setName(message.getText());
        return TelegramMessageUtil.buildRequestFieldMessage(message.getChatId(), ENTER_BURNED_CALORIES_MESSAGE);
    }

    private SendMessage handleBurnedCaloriesField(Activity activity, Message message) {
        String input = message.getText();
        Optional<Double> optionalCalories = parseCalories(input);
        if (optionalCalories.isEmpty() || optionalCalories.get() <= 0) {
            return TelegramMessageUtil.buildRequestFieldMessage(message.getChatId(), INCORRECT_INPUT_FORMAT_MESSAGE);
        }
        activity.setBurnedCalories(optionalCalories.get());
        return TelegramMessageUtil.buildRequestFieldMessage(message.getChatId(), ENTER_SPENT_TIME_MESSAGE);
    }

    private Optional<Double> parseCalories(String input) {
        try {
            return Optional.of(Double.parseDouble(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private SendMessage handleSpentTimeInMinutesField(Activity activity, Message message) {
        String input = message.getText();
        Optional<Integer> optionalMinutes = parseMinutes(input);
        if (optionalMinutes.isEmpty() || optionalMinutes.get() <= 0) {
            return TelegramMessageUtil.buildRequestFieldMessage(message.getChatId(), INCORRECT_INPUT_FORMAT_MESSAGE);
        }
        activity.setSpentTimeInMinutes(optionalMinutes.get());
        return TelegramMessageUtil.buildRequestFieldMessage(message.getChatId(), ENTER_NOTES_MESSAGE);
    }

    private Optional<Integer> parseMinutes(String input) {
        try {
            return Optional.of(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private SendMessage handleNotesField(Activity activity, Message message) {
        activity.setNotes(message.getText());
        activityRepository.save(activity);
        activityHolder.removeActivity(message.getChatId());
        SendMessage response = TelegramMessageUtil.buildRequestFieldMessage(message.getChatId(), ACTIVITY_ADDED_MESSAGE);
        ActivitiesMessageStrategy activitiesMessageStrategy = new ActivitiesMessageStrategy(applicationEventPublisher);
        response.setReplyMarkup(activitiesMessageStrategy.getReplyKeyboardMarkup());
        return response;
    }

}