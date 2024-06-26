package ua.bibusukraine.fitlifebot.telegram.command.strategy.activity;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.cache.ActivityHolder;
import ua.bibusukraine.fitlifebot.model.Activity;
import ua.bibusukraine.fitlifebot.model.RequestFieldMessage;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.repository.ActivityRepository;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

import java.util.Optional;

@Component
public class RemoveActivityMessageStrategy implements TelegramMessageStrategy {

    private static final String ENTER_ACTIVITY_ID_MESSAGE = "Enter the ID of the activity you want to remove";
    private static final String INCORRECT_INPUT_FORMAT_MESSAGE = "Incorrect input format";
    private static final String ACTIVITY_REMOVED_MESSAGE = "Activity successfully removed";
    private static final String ACTIVITY_NOT_FOUND_MESSAGE = "Activity with such ID not found";

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ActivityRepository activityRepository;
    private final ActivityHolder activityHolder;

    public RemoveActivityMessageStrategy(ApplicationEventPublisher applicationEventPublisher, ActivityRepository activityRepository, ActivityHolder activityHolder) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.activityRepository = activityRepository;
        this.activityHolder = activityHolder;
    }

    @Override
    public void execute(Message message) {
        Activity removedActivity = activityHolder.getActivity(message.getChatId());
        if (removedActivity == null) {
            handleIdRequestMessage(message);
        } else {
            handleIdMessage(message);
        }
    }

    private void handleIdRequestMessage(Message message) {
        Activity removedActivity = new Activity();
        removedActivity.setChatId(message.getChatId());
        activityHolder.putActivity(message.getChatId(), removedActivity);
        SendMessage response = new RequestFieldMessage(message.getChatId().toString(), ENTER_ACTIVITY_ID_MESSAGE);
        applicationEventPublisher.publishEvent(response);
    }

    private void handleIdMessage(Message message) {
        Optional<Long> activityId = parseLong(message.getText());
        if (activityId.isEmpty()) {
            SendMessage response = new RequestFieldMessage(message.getChatId().toString(), INCORRECT_INPUT_FORMAT_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        } else {
            Optional<Activity> existingActivity = activityRepository.findById(activityId.get());
            if (existingActivity.isEmpty() || !existingActivity.get().getChatId().equals(message.getChatId())) {
                activityHolder.removeActivity(message.getChatId());
                SendMessage response = new SendMessage(message.getChatId().toString(), ACTIVITY_NOT_FOUND_MESSAGE);
                ActivitiesMessageStrategy activitiesMessageStrategy = new ActivitiesMessageStrategy(applicationEventPublisher);
                response.setReplyMarkup(activitiesMessageStrategy.getReplyKeyboardMarkup());
                applicationEventPublisher.publishEvent(response);
            } else {
                removeActivity(message, activityId.get());
            }
        }
    }

    private void removeActivity(Message message, Long activityId) {
        activityRepository.deleteById(activityId);
        activityHolder.removeActivity(message.getChatId());
        SendMessage response = new SendMessage(message.getChatId().toString(), ACTIVITY_REMOVED_MESSAGE);
        ActivitiesMessageStrategy activitiesMessageStrategy = new ActivitiesMessageStrategy(applicationEventPublisher);
        response.setReplyMarkup(activitiesMessageStrategy.getReplyKeyboardMarkup());
        applicationEventPublisher.publishEvent(response);
    }

    public Optional<Long> parseLong(String text) {
        try {
            return Optional.of(Long.parseLong(text));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.REMOVE_ACTIVITY;
    }

}
