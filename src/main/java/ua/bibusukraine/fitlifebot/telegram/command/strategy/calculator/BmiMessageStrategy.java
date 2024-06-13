package ua.bibusukraine.fitlifebot.telegram.command.strategy.calculator;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.cache.BmiDataHolder;
import ua.bibusukraine.fitlifebot.model.BmiData;
import ua.bibusukraine.fitlifebot.model.RequestFieldMessage;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

import java.util.Optional;

@Component
public class BmiMessageStrategy implements TelegramMessageStrategy {

    private static final String ENTER_WEIGHT_MESSAGE = "Enter your weight in kg";
    private static final String ENTER_HEIGHT_MESSAGE = "Enter your height in cm";
    private static final String INCORRECT_INPUT_FORMAT_MESSAGE = "Incorrect input format";
    private static final String BMI_RESULT_MESSAGE = """
            Your BMI is %.2s. It is %s.
            
            Underweight: BMI < 18.5
            Normal weight: BMI 18.5–24.9
            Overweight: BMI 25–29.9
            Obesity: BMI 30 or greater""";

    private final BmiDataHolder bmiDataHolder;
    private final ApplicationEventPublisher applicationEventPublisher;

    public BmiMessageStrategy(BmiDataHolder bmiDataHolder, ApplicationEventPublisher applicationEventPublisher) {
        this.bmiDataHolder = bmiDataHolder;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void execute(Message message) {
        BmiData bmiData = bmiDataHolder.getBmiData(message.getChatId());
        if (bmiData == null) {
            handleStartMessage(message);
        } else if (bmiData.getWeight() == null) {
           handleWeightMessage(message, bmiData);
        } else if (bmiData.getHeight() == null) {
            handleHeightMessage(message, bmiData);
        }
    }

    private void handleStartMessage(Message message) {
        BmiData bmiData = new BmiData();
        bmiDataHolder.putBmiData(message.getChatId(), bmiData);
        SendMessage response = new SendMessage(message.getChatId().toString(), ENTER_WEIGHT_MESSAGE);
        applicationEventPublisher.publishEvent(response);
    }

    private void handleWeightMessage(Message message, BmiData bmiData) {
        Optional<Double> weight = parseDouble(message.getText());
        if (weight.isEmpty()) {
            RequestFieldMessage response = new RequestFieldMessage(message.getChatId().toString(), INCORRECT_INPUT_FORMAT_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        } else {
            if (weight.get() <= 0) {
                RequestFieldMessage response = new RequestFieldMessage(message.getChatId().toString(), INCORRECT_INPUT_FORMAT_MESSAGE);
                applicationEventPublisher.publishEvent(response);
            } else {
                bmiData.setWeight(weight.get());
                bmiDataHolder.putBmiData(message.getChatId(), bmiData);
                RequestFieldMessage response = new RequestFieldMessage(message.getChatId().toString(), ENTER_HEIGHT_MESSAGE);
                applicationEventPublisher.publishEvent(response);
            }
        }
    }

    private void handleHeightMessage(Message message, BmiData bmiData) {
        Optional<Double> height = parseDouble(message.getText());
        if (height.isEmpty()) {
            RequestFieldMessage response = new RequestFieldMessage(message.getChatId().toString(), INCORRECT_INPUT_FORMAT_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        } else {
            if (height.get() <= 0) {
                RequestFieldMessage response = new RequestFieldMessage(message.getChatId().toString(), INCORRECT_INPUT_FORMAT_MESSAGE);
                applicationEventPublisher.publishEvent(response);
            } else {
                bmiData.setHeight(height.get());
                double bmi = calculateBmi(bmiData.getWeight(), bmiData.getHeight());
                RequestFieldMessage response = new RequestFieldMessage(message.getChatId().toString(), String.format(BMI_RESULT_MESSAGE, bmi, getBmiCategory(bmi)));
                applicationEventPublisher.publishEvent(response);
                bmiDataHolder.removeBmiData(message.getChatId());
            }
        }
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.BMI;
    }

    private Optional<Double> parseDouble(String input) {
        try {
            return Optional.of(Double.parseDouble(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private double calculateBmi(double weight, double height) {
        return weight / Math.pow(height / 100, 2);
    }

    private String getBmiCategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal weight";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obesity";
        }
    }

}
