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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class AddWeightMessageStrategy implements TelegramMessageStrategy {

    private static final String ENTER_WEIGHT_MESSAGE = "Enter the current weight";
    private static final String ENTER_DATE_WEIGHT_MESSAGE = "Enter the date :";
    private static final String INCORRECT_INPUT_FORMAT_MESSAGE = "Invalid input format. Please enter your weight in kilograms (e.g., 70.5)";
    private static final String INCORRECT_DATE_INPUT_FORMAT_MESSAGE = "Invalid date input format. dd.mm.yyyy";
    private final ApplicationEventPublisher applicationEventPublisher;
    private final WeightHolder weightHolder;
    private final WeightRepository weightRepository;

    public AddWeightMessageStrategy(ApplicationEventPublisher applicationEventPublisher, WeightHolder weightHolder, WeightRepository weightRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.weightHolder = weightHolder;
        this.weightRepository = weightRepository;
    }

    @Override
    public void execute(Message message) {
        Weight weight = weightHolder.getWeight(message.getChatId());
        if (weight == null) {
            weight = new Weight();
            weight.setChatId(message.getChatId());
            weightHolder.putWeight(message.getChatId(), weight);
            SendMessage response = new RequestFieldMessage(message.getChatId().toString(), ENTER_DATE_WEIGHT_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        } else {
            handleInput(weight, message);
        }
    }

    private void handleInput(Weight weight, Message message) {
        if (weight.getCreatedAt() == null) {
            handleDateInput(weight, message);
        } else if (weight.getWeight() == null) {
            handleWeightInput(weight, message);
        }
    }

    private void handleDateInput(Weight weight, Message message) {
        String input = message.getText();
        try {
            if (!isValidDateFormat(input)) {
                throw new DateTimeParseException("Invalid format", input, 0);
            }

            LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            LocalDateTime dateTime = date.atStartOfDay();
            weight.setCreatedAt(dateTime);

            SendMessage response = new RequestFieldMessage(message.getChatId().toString(), ENTER_WEIGHT_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        } catch (DateTimeParseException e) {
            SendMessage retryMessage = new RequestFieldMessage(message.getChatId().toString(), INCORRECT_DATE_INPUT_FORMAT_MESSAGE);
            applicationEventPublisher.publishEvent(retryMessage);
        }
    }


    private void handleWeightInput(Weight weight, Message message) {
        String input = message.getText();
        try {
            if (!isValidWeightFormat(input)){
                throw new NumberFormatException();
            }

            BigDecimal userWeight = BigDecimal.valueOf(Double.parseDouble(input));
            weight.setWeight(userWeight);

            weightRepository.save(weight);

            SendMessage response = new SendMessage(message.getChatId().toString(), "Your weight has been successfully added: " + userWeight + " kg");
            WeightMessageStrategy weightsMessageStrategy = new WeightMessageStrategy(applicationEventPublisher);
            response.setReplyMarkup(weightsMessageStrategy.getReplyKeyboardMarkup());
            applicationEventPublisher.publishEvent(response);
        } catch (NumberFormatException e) {
            SendMessage retryMessage = new RequestFieldMessage(message.getChatId().toString(), INCORRECT_INPUT_FORMAT_MESSAGE);
            applicationEventPublisher.publishEvent(retryMessage);
        }
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.ADD_WEIGHT;
    }

    private boolean isValidWeightFormat(String input) {
        try {
            double weight = Double.parseDouble(input);
            return weight > 0 && weight <= 300;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isValidDateFormat(String input) {
        try {
            LocalDate.parse(input, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


}
