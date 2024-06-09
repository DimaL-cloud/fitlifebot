package ua.bibusukraine.fitlifebot.telegram.command.strategy.weight;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.cache.WeightHolder;
import ua.bibusukraine.fitlifebot.model.RequestFieldMessage;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.model.Weight;
import ua.bibusukraine.fitlifebot.repository.WeightRepository;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

import java.util.Optional;

@Component
public class RemoveWeightMessageStrategy implements TelegramMessageStrategy {

    private static final String ENTER_WEIGHT_ID_MESSAGE = "Введіть айді запису ваги, яку ви хочете видалити";
    private static final String WEIGHT_REMOVED_MESSAGE = "Запис ваги успішно видалена";
    private static final String WEIGHT_NOT_FOUND_MESSAGE = "Запис ваги з таким айді не знайдена";

    private final ApplicationEventPublisher applicationEventPublisher;
    private final WeightRepository weightRepository;
    private final WeightHolder weightHolder;

    public RemoveWeightMessageStrategy(ApplicationEventPublisher applicationEventPublisher, WeightRepository weightRepository, WeightHolder weightHolder) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.weightRepository = weightRepository;
        this.weightHolder = weightHolder;
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
        Weight removedWeight = new Weight();
        removedWeight.setChatId(message.getChatId());
        weightHolder.putWeight(message.getChatId(), removedWeight);
        SendMessage response = new RequestFieldMessage(message.getChatId().toString(), ENTER_WEIGHT_ID_MESSAGE);
        applicationEventPublisher.publishEvent(response);
    }

    private void handleIdMessage(Message message, Long activityId) {
        Optional<Weight> existingActivity = weightRepository.findById(activityId);
        if (existingActivity.isEmpty() || !existingActivity.get().getChatId().equals(message.getChatId())) {
            handleInvalidActivity(message);
        } else {
            removeActivity(message, activityId);
        }
    }

    private void handleInvalidActivity(Message message) {
        weightHolder.removeWeight(message.getChatId());
        SendMessage response = new SendMessage(message.getChatId().toString(), WEIGHT_NOT_FOUND_MESSAGE);
        WeightMessageStrategy weightsMessageStrategy = new WeightMessageStrategy(applicationEventPublisher);
        response.setReplyMarkup(weightsMessageStrategy.getReplyKeyboardMarkup());
        applicationEventPublisher.publishEvent(response);
    }

    private void removeActivity(Message message, Long activityId) {
        weightRepository.deleteById(activityId);
        weightHolder.removeWeight(message.getChatId());
        SendMessage response = new SendMessage(message.getChatId().toString(), WEIGHT_REMOVED_MESSAGE);
        WeightMessageStrategy weightsMessageStrategy = new WeightMessageStrategy(applicationEventPublisher);
        response.setReplyMarkup(weightsMessageStrategy.getReplyKeyboardMarkup());
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
        return TelegramCommand.REMOVE_WEIGHT;
    }

}
