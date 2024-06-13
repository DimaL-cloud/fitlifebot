package ua.bibusukraine.fitlifebot.telegram.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.cache.CommandHolder;
import ua.bibusukraine.fitlifebot.model.TelegramCommand;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.StartMessageStrategy;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.TelegramMessageStrategy;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.UnrecognisedMessageStrategy;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.weight.AddWeightMessageStrategy;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.weight.WeightMessageStrategy;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TelegramMessageStrategyContextTest {

    @Mock
    private CommandHolder commandHolder;

    @Mock
    private StartMessageStrategy startMessageStrategy;

    @Mock
    private WeightMessageStrategy weightMessageStrategy;

    @Mock
    private AddWeightMessageStrategy addWeightMessageStrategy;

    @Mock
    private UnrecognisedMessageStrategy unrecognisedMessageStrategy;

    private TelegramMessageStrategyContext telegramMessageStrategyContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(startMessageStrategy.getCommand()).thenReturn(TelegramCommand.START);
        when(weightMessageStrategy.getCommand()).thenReturn(TelegramCommand.WEIGHT);
        when(addWeightMessageStrategy.getCommand()).thenReturn(TelegramCommand.ADD_WEIGHT);
        when(unrecognisedMessageStrategy.getCommand()).thenReturn(TelegramCommand.UNRECOGNISED);
        List<TelegramMessageStrategy> strategies = Arrays.asList(startMessageStrategy, weightMessageStrategy, addWeightMessageStrategy, unrecognisedMessageStrategy);
        telegramMessageStrategyContext = new TelegramMessageStrategyContext(strategies, commandHolder);
    }

    @Test
    void getStrategyReturnsCorrectStrategyForRecognisedCommand() {
        Message message = mock(Message.class);
        when(message.getText()).thenReturn("/start");

        TelegramMessageStrategy telegramMessageStrategy = telegramMessageStrategyContext.getStrategy(message);

        assertInstanceOf(StartMessageStrategy.class, telegramMessageStrategy);
        verify(commandHolder, times(1)).getLastUserCommand(any(Long.class));
        verify(startMessageStrategy, times(1)).getCommand();
        verify(weightMessageStrategy, times(1)).getCommand();
    }

    @Test
    void getStrategyReturnsUnrecognisedStrategyForUnrecognisedCommand() {
        Message message = mock(Message.class);
        when(message.getText()).thenReturn("123");

        TelegramMessageStrategy telegramMessageStrategy = telegramMessageStrategyContext.getStrategy(message);

        assertInstanceOf(UnrecognisedMessageStrategy.class, telegramMessageStrategy);
        verify(commandHolder, times(1)).getLastUserCommand(any(Long.class));
        verify(startMessageStrategy, times(1)).getCommand();
        verify(weightMessageStrategy, times(1)).getCommand();
    }

    @Test
    void getStrategyReturnsCorrectStrategyForUnrecognisedCommand() {
        Message message = mock(Message.class);
        when(message.getText()).thenReturn("123");
        when(commandHolder.getLastUserCommand(any(Long.class))).thenReturn(TelegramCommand.ADD_WEIGHT);

        TelegramMessageStrategy telegramMessageStrategy = telegramMessageStrategyContext.getStrategy(message);

        assertInstanceOf(AddWeightMessageStrategy.class, telegramMessageStrategy);
        verify(commandHolder, times(1)).getLastUserCommand(any(Long.class));
        verify(startMessageStrategy, times(1)).getCommand();
        verify(weightMessageStrategy, times(1)).getCommand();
    }

}