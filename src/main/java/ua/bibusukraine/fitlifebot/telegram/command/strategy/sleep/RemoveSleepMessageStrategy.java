package ua.bibusukraine.fitlifebot.telegram.command.strategy.sleep;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.cache.SleepHolder;
import ua.bibusukraine.fitlifebot.model.RequestFieldMessage;
import ua.bibusukraine.fitlifebot.model.Sleep;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.repository.SleepRepository;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

import java.util.Optional;

@Component
public class RemoveSleepMessageStrategy implements TelegramMessageStrategy {

    private static final String ENTER_SLEEP_ID_MESSAGE = "Enter the ID of the sleep record you want to remove";
    private static final String INCORRECT_INPUT_FORMAT_MESSAGE = "Incorrect input format";
    private static final String SLEEP_REMOVED_MESSAGE = "Sleep record successfully removed";
    private static final String SLEEP_NOT_FOUND_MESSAGE = "Sleep record with such ID not found";

    private final ApplicationEventPublisher applicationEventPublisher;
    private final SleepRepository sleepRepository;
    private final SleepHolder sleepHolder;

    public RemoveSleepMessageStrategy(ApplicationEventPublisher applicationEventPublisher, SleepRepository sleepRepository, SleepHolder sleepHolder) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.sleepRepository = sleepRepository;
        this.sleepHolder = sleepHolder;
    }

    @Override
    public void execute(Message message) {
        Optional<Long> activityId = parseLong(message.getText());
        if (activityId.isEmpty()) {
            handleIdRequestMessage(message);
        } else {
            handleIdMessage(message, activityId.get());
        }
    }

    private void handleIdRequestMessage(Message message) {
        Sleep removedSleep = new Sleep();
        removedSleep.setChatId(message.getChatId());
        sleepHolder.putSleep(message.getChatId(), removedSleep);
        SendMessage response = new RequestFieldMessage(message.getChatId().toString(), ENTER_SLEEP_ID_MESSAGE);
        applicationEventPublisher.publishEvent(response);
    }

    private void handleIdMessage(Message message, Long activityId) {
        Optional<Sleep> existingActivity = sleepRepository.findById(activityId);
        if (existingActivity.isEmpty() || !existingActivity.get().getChatId().equals(message.getChatId())) {
            handleInvalidActivity(message);
        } else {
            removeActivity(message, activityId);
        }
    }

    private void handleInvalidActivity(Message message) {
        sleepHolder.removeSleep(message.getChatId());
        SendMessage response = new SendMessage(message.getChatId().toString(), SLEEP_NOT_FOUND_MESSAGE);
        SleepMessageStrategy activitiesMessageStrategy = new SleepMessageStrategy(applicationEventPublisher);
        response.setReplyMarkup(activitiesMessageStrategy.getReplyKeyboardMarkup());
        applicationEventPublisher.publishEvent(response);
    }

    private void removeActivity(Message message, Long activityId) {
        sleepRepository.deleteById(activityId);
        sleepHolder.removeSleep(message.getChatId());
        SendMessage response = new SendMessage(message.getChatId().toString(), SLEEP_REMOVED_MESSAGE);
        SleepMessageStrategy activitiesMessageStrategy = new SleepMessageStrategy(applicationEventPublisher);
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
        return TelegramCommand.REMOVE_SLEEP;
    }

}
