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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class AddSleepMessageStrategy implements TelegramMessageStrategy {

    private static final String ENTER_WENT_TO_SLEEP_MESSAGE = "Enter the time and date you went to sleep (HH:mm dd.MM.yyyy)";
    private static final String ENTER_SPENT_TIME_MESSAGE = "Enter the time and date you woke up (HH:mm dd.MM.yyyy)";
    private static final String SLEEP_ADDED_MESSAGE = "Sleep record successfully added";
    private static final String INCORRECT_INPUT_FORMAT_MESSAGE = "Incorrect input format. Please use HH:mm dd.MM.yyyy";
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SleepHolder sleepHolder;
    private final SleepRepository sleepRepository;

    public AddSleepMessageStrategy(ApplicationEventPublisher applicationEventPublisher,
                                   SleepHolder sleepHolder, SleepRepository sleepRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.sleepHolder = sleepHolder;
        this.sleepRepository = sleepRepository;
    }

    @Override
    public void execute(Message message) {
        SendMessage response;
        Sleep sleep = sleepHolder.getSleep(message.getChatId());
        if (sleep == null) {
            sleep = new Sleep();
            sleep.setChatId(message.getChatId());
            sleepHolder.putSleep(message.getChatId(), sleep);
            response = new RequestFieldMessage(message.getChatId().toString(), ENTER_WENT_TO_SLEEP_MESSAGE);
        } else if (sleep.getWentToBed() == null) {
            response = handleInputWentToSleepTime(sleep, message);
        } else if (sleep.getWokeUp() == null) {
            response = handleInputWokeUpTime(sleep, message);
        } else {
            response = new RequestFieldMessage(message.getChatId().toString(), "Both times have already been entered.");
        }
        applicationEventPublisher.publishEvent(response);
    }

    private SendMessage handleInputWentToSleepTime(Sleep sleep, Message message) {
        String input = message.getText();
        try {
            LocalDateTime wentToSleepTime = LocalDateTime.parse(input, DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
            sleep.setWentToBed(wentToSleepTime);
            return new RequestFieldMessage(message.getChatId().toString(), ENTER_SPENT_TIME_MESSAGE);
        } catch (DateTimeParseException e) {
            return new RequestFieldMessage(message.getChatId().toString(), INCORRECT_INPUT_FORMAT_MESSAGE);
        }
    }

    private SendMessage handleInputWokeUpTime(Sleep sleep, Message message) {
        String input = message.getText();
        try {
            LocalDateTime wokeUpTime = LocalDateTime.parse(input, DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"));
            sleep.setWokeUp(wokeUpTime);
            sleep.setSleepTimeInMinutes(calculateTimeSleep(sleep.getWentToBed(), sleep.getWokeUp()));
            sleepRepository.save(sleep);

            SendMessage response = new RequestFieldMessage(message.getChatId().toString(), SLEEP_ADDED_MESSAGE);
            SleepMessageStrategy sleepMessageStrategy = new SleepMessageStrategy(applicationEventPublisher);
            response.setReplyMarkup(sleepMessageStrategy.getReplyKeyboardMarkup());
            return response;
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return new RequestFieldMessage(message.getChatId().toString(), INCORRECT_INPUT_FORMAT_MESSAGE);
        }
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.ADD_SLEEP;
    }

    private Long calculateTimeSleep(LocalDateTime wentToSleep, LocalDateTime wokeUp) {
        long minutes = Duration.between(wentToSleep, wokeUp).toMinutes();
        if (minutes < 0) {
            throw new IllegalArgumentException("Minutes can't be less than 0");
        }
        return Duration.between(wentToSleep, wokeUp).toMinutes();
    }
}