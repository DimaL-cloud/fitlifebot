package ua.bibusukraine.fitlifebot.telegram.command.strategy.calculator;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.cache.BmrDataHolder;
import ua.bibusukraine.fitlifebot.model.BmrData;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

import java.util.Optional;

@Component
public class BmrMessageStrategy implements TelegramMessageStrategy {

    private static final String ENTER_WEIGHT_MESSAGE = "Enter your weight in kg";
    private static final String ENTER_HEIGHT_MESSAGE = "Enter your height in cm";
    private static final String ENTER_AGE_MESSAGE = "Enter your age in years";
    private static final String ENTER_GENDER_MESSAGE = "Enter your gender (male/female)";
    private static final String INCORRECT_INPUT_FORMAT_MESSAGE = "Incorrect input format";
    private static final String BMR_RESULT_MESSAGE = "Your BMR is %.2f calories/day";
    private static final String MALE = "male";
    private static final String FEMALE = "female";

    private final BmrDataHolder bmrDataHolder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CalculatorsMessageStrategy calculatorsMessageStrategy;

    public BmrMessageStrategy(BmrDataHolder bmrDataHolder,
                              ApplicationEventPublisher applicationEventPublisher,
                              CalculatorsMessageStrategy calculatorsMessageStrategy) {
        this.bmrDataHolder = bmrDataHolder;
        this.applicationEventPublisher = applicationEventPublisher;
        this.calculatorsMessageStrategy = calculatorsMessageStrategy;
    }

    @Override
    public void execute(Message message) {
        BmrData bmrData = bmrDataHolder.getBmrData(message.getChatId());
        if (bmrData == null) {
            handleStartMessage(message);
        } else if (bmrData.getWeight() == null) {
            handleWeightMessage(message, bmrData);
        } else if (bmrData.getHeight() == null) {
            handleHeightMessage(message, bmrData);
        } else if (bmrData.getAge() == null) {
            handleAgeMessage(message, bmrData);
        } else if (bmrData.getGender() == null) {
            handleGenderMessage(message, bmrData);
        }
    }

    private void handleStartMessage(Message message) {
        BmrData bmrData = new BmrData();
        bmrDataHolder.putBmrData(message.getChatId(), bmrData);
        SendMessage response = new SendMessage(message.getChatId().toString(), ENTER_WEIGHT_MESSAGE);
        applicationEventPublisher.publishEvent(response);
    }

    private void handleWeightMessage(Message message, BmrData bmrData) {
        Optional<Double> weight = parseDouble(message.getText());
        if (weight.isEmpty() || weight.get() <= 0) {
            SendMessage response = new SendMessage(message.getChatId().toString(), INCORRECT_INPUT_FORMAT_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        } else {
            bmrData.setWeight(weight.get());
            bmrDataHolder.putBmrData(message.getChatId(), bmrData);
            SendMessage response = new SendMessage(message.getChatId().toString(), ENTER_HEIGHT_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        }
    }

    private void handleHeightMessage(Message message, BmrData bmrData) {
        Optional<Double> height = parseDouble(message.getText());
        if (height.isEmpty() || height.get() <= 0) {
            SendMessage response = new SendMessage(message.getChatId().toString(), INCORRECT_INPUT_FORMAT_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        } else {
            bmrData.setHeight(height.get());
            bmrDataHolder.putBmrData(message.getChatId(), bmrData);
            SendMessage response = new SendMessage(message.getChatId().toString(), ENTER_AGE_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        }
    }

    private void handleAgeMessage(Message message, BmrData bmrData) {
        Optional<Integer> age = parseInteger(message.getText());
        if (age.isEmpty() || age.get() <= 0) {
            SendMessage response = new SendMessage(message.getChatId().toString(), INCORRECT_INPUT_FORMAT_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        } else {
            bmrData.setAge(age.get());
            bmrDataHolder.putBmrData(message.getChatId(), bmrData);
            SendMessage response = new SendMessage(message.getChatId().toString(), ENTER_GENDER_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        }
    }

    private void handleGenderMessage(Message message, BmrData bmrData) {
        String gender = message.getText().trim().toLowerCase();
        if (!gender.equals(MALE) && !gender.equals(FEMALE)) {
            SendMessage response = new SendMessage(message.getChatId().toString(), INCORRECT_INPUT_FORMAT_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        } else {
            bmrData.setGender(gender);
            double bmr = calculateBmr(bmrData.getWeight(), bmrData.getHeight(), bmrData.getAge(), bmrData.getGender());
            SendMessage response = new SendMessage(message.getChatId().toString(), String.format(BMR_RESULT_MESSAGE, bmr));
            response.setReplyMarkup(calculatorsMessageStrategy.getReplyKeyboardMarkup());
            applicationEventPublisher.publishEvent(response);
            bmrDataHolder.removeBmrData(message.getChatId());
        }
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.BMR;
    }

    private Optional<Double> parseDouble(String input) {
        try {
            return Optional.of(Double.parseDouble(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private Optional<Integer> parseInteger(String input) {
        try {
            return Optional.of(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private double calculateBmr(double weight, double height, int age, String gender) {
        if (gender.equals(MALE)) {
            return 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            return 10 * weight + 6.25 * height - 5 * age - 161;
        }
    }

}
