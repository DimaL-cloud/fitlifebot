package ua.bibusukraine.fitlifebot.telegram.command.strategy.calculator;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.cache.WhrDataHolder;
import ua.bibusukraine.fitlifebot.model.RequestFieldMessage;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.model.WhrData;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;

import java.util.Optional;

@Component
public class WhrMessageStrategy implements TelegramMessageStrategy {

    private static final String ENTER_WAIST_MESSAGE = "Enter your waist circumference in cm";
    private static final String ENTER_HIPS_MESSAGE = "Enter your hip circumference in cm";
    private static final String INCORRECT_INPUT_FORMAT_MESSAGE = "Incorrect input format";
    private static final String WHR_RESULT_MESSAGE = """
            Your Waist-to-Hip Ratio (WHR) is %.2f. Сategory: %s
        
            Low Risk (Healthy Range): WHR < 0.9
            Moderate Risk: 0.9 ≤ WHR < 1.0
            High Risk (Apple-shaped): WHR ≥ 1.0
            """;


    private final WhrDataHolder whrDataHolder;
    private final CalculatorsMessageStrategy calculatorsMessageStrategy;
    private final ApplicationEventPublisher applicationEventPublisher;

    public WhrMessageStrategy(WhrDataHolder whrDataHolder,
                              CalculatorsMessageStrategy calculatorsMessageStrategy,
                              ApplicationEventPublisher applicationEventPublisher) {
        this.whrDataHolder = whrDataHolder;
        this.calculatorsMessageStrategy = calculatorsMessageStrategy;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void execute(Message message) {
        WhrData whrData = whrDataHolder.getWhrData(message.getChatId());
        if (whrData == null) {
            handleStartMessage(message);
        } else if (whrData.getWaist() == null) {
            handleWaistMessage(message, whrData);
        } else if (whrData.getHips() == null) {
            handleHipsMessage(message, whrData);
        }
    }

    private void handleStartMessage(Message message) {
        WhrData whrData = new WhrData();
        whrDataHolder.putWhrData(message.getChatId(), whrData);
        SendMessage response = new RequestFieldMessage(message.getChatId().toString(), ENTER_WAIST_MESSAGE);
        applicationEventPublisher.publishEvent(response);
    }

    private void handleWaistMessage(Message message, WhrData whrData) {
        Optional<Double> waist = parseDouble(message.getText());
        if (waist.isEmpty() || waist.get() <= 0) {
            RequestFieldMessage response = new RequestFieldMessage(message.getChatId().toString(), INCORRECT_INPUT_FORMAT_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        } else {
            whrData.setWaist(waist.get());
            whrDataHolder.putWhrData(message.getChatId(), whrData);
            RequestFieldMessage response = new RequestFieldMessage(message.getChatId().toString(), ENTER_HIPS_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        }
    }

    private void handleHipsMessage(Message message, WhrData whrData) {
        Optional<Double> hips = parseDouble(message.getText());
        if (hips.isEmpty() || hips.get() <= 0) {
            RequestFieldMessage response = new RequestFieldMessage(message.getChatId().toString(), INCORRECT_INPUT_FORMAT_MESSAGE);
            applicationEventPublisher.publishEvent(response);
        } else {
            whrData.setHips(hips.get());
            whrDataHolder.putWhrData(message.getChatId(), whrData);
            double whr = calculateWHR(whrData.getWaist(), whrData.getHips());
            SendMessage response = new SendMessage(message.getChatId().toString(), String.format(WHR_RESULT_MESSAGE, whr, getWhrCategory(whr)));
            response.setReplyMarkup(calculatorsMessageStrategy.getReplyKeyboardMarkup());
            applicationEventPublisher.publishEvent(response);
            whrDataHolder.removeWhrData(message.getChatId());
        }
    }

    @Override
    public TelegramCommand getCommand() {
        return TelegramCommand.WHR;
    }

    private Optional<Double> parseDouble(String input) {
        try {
            return Optional.of(Double.parseDouble(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private double calculateWHR(Double waist, Double hips) {
        if (waist == null || hips == null) {
            throw new IllegalArgumentException("Waist and hips must be provided.");
        }
        return waist / hips;
    }

    private String getWhrCategory(double whr) {
        if (whr < 0.9) {
            return "Low Risk (Healthy Range)";
        } else if (whr < 1.0) {
            return "Moderate Risk";
        } else {
            return "High Risk (Apple-shaped)";
        }
    }

}
