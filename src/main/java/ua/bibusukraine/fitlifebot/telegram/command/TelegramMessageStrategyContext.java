package ua.bibusukraine.fitlifebot.telegram.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.repository.ActivityRepository;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.StartMessageStrategy;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.UnrecognisedMessageStrategy;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.activity.ActivitiesMessageStrategy;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.activity.AddActivityMessageStrategy;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.activity.GetActivitiesMessageStrategy;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.activity.RemoveActivityMessageStrategy;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.nutrition.NutritionMessageStrategy;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.sleep.SleepMessageStrategy;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.weight.WeightMessageStrategy;

@Component
public class TelegramMessageStrategyContext {

    private final CommandHolder commandHolder;
    private final ActivityHolder activityHolder;
    private final ActivityRepository activityRepository;

    public TelegramMessageStrategyContext(CommandHolder commandHolder,
                                          ActivityHolder activityHolder,
                                          ActivityRepository activityRepository) {
        this.commandHolder = commandHolder;
        this.activityHolder = activityHolder;
        this.activityRepository = activityRepository;
    }

    public TelegramMessageStrategy getStrategy(Message message) {
        TelegramCommand command = TelegramCommand.getTelegramCommandByText(message.getText());
        TelegramCommand lastUserCommand = commandHolder.getLastUserCommand(message.getChatId());
        command = resolveUnrecognisedCommand(command, lastUserCommand);
        TelegramMessageStrategy telegramMessageStrategy = createStrategy(command, message);
        updateLastUserCommand(command, message.getChatId());
        return telegramMessageStrategy;
    }

    private TelegramCommand resolveUnrecognisedCommand(TelegramCommand command, TelegramCommand lastUserCommand) {
        if (command == TelegramCommand.UNRECOGNISED && lastUserCommand != null) {
            command = switch (lastUserCommand) {
                case ADD_ACTIVITY -> TelegramCommand.ADD_ACTIVITY;
                case REMOVE_ACTIVITY -> TelegramCommand.REMOVE_ACTIVITY;
                default -> command;
            };
        }
        return command;
    }

    private TelegramMessageStrategy createStrategy(TelegramCommand command, Message message) {
        return switch (command) {
            case START, BACK -> new StartMessageStrategy(message);
            case ACTIVITIES -> new ActivitiesMessageStrategy(message);
            case NUTRITION -> new NutritionMessageStrategy(message);
            case WEIGHT -> new WeightMessageStrategy(message);
            case SLEEP -> new SleepMessageStrategy(message);
            case ADD_ACTIVITY -> new AddActivityMessageStrategy(message, commandHolder, activityHolder, activityRepository);
            case REMOVE_ACTIVITY -> new RemoveActivityMessageStrategy(message, activityRepository);
            case GET_ACTIVITIES_REPORT -> new GetActivitiesMessageStrategy(message);
            case UNRECOGNISED -> new UnrecognisedMessageStrategy(message);
        };
    }

    private void updateLastUserCommand(TelegramCommand command, Long chatId) {
        if (command != TelegramCommand.UNRECOGNISED) {
            commandHolder.putLastUserCommand(chatId, command);
        }
    }

}
