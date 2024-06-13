package ua.bibusukraine.fitlifebot.sleepTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.cache.SleepHolder;
import ua.bibusukraine.fitlifebot.model.Sleep;
import ua.bibusukraine.fitlifebot.repository.SleepRepository;
import ua.bibusukraine.fitlifebot.telegram.command.strategy.sleep.AddSleepMessageStrategy;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SleepMessageStrategyTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private SleepHolder sleepHolder;

    @Mock
    private SleepRepository sleepRepository;

    @InjectMocks
    private AddSleepMessageStrategy addSleepMessageStrategy;

    @Mock
    private Message message;

    @BeforeEach
    void setUp() {
        addSleepMessageStrategy = new AddSleepMessageStrategy(applicationEventPublisher, sleepHolder, sleepRepository);
    }

    @Test
    void executeCreatesNewSleepRecordWhenNoneExists() {
        when(message.getChatId()).thenReturn(12345L);
        when(sleepHolder.getSleep(12345L)).thenReturn(null);

        addSleepMessageStrategy.execute(message);

        verify(sleepHolder).putSleep(eq(12345L), any(Sleep.class));
    }

    @Test
    void executeRequestsWentToSleepTimeWhenOnlyWokeUpIsEntered() {
        long chatId = 12345L;
        when(message.getChatId()).thenReturn(chatId);
        when(message.getText()).thenReturn("22:00 01.01.2023");

        Sleep sleep = new Sleep();
        sleep.setChatId(chatId);
        sleep.setWokeUp(LocalDateTime.now());
        when(sleepHolder.getSleep(chatId)).thenReturn(sleep);

        addSleepMessageStrategy.execute(message);

        verify(sleepHolder, times(1)).getSleep(any(Long.class));
    }

    @Test
    void executeDoesNotOverwriteExistingSleepRecord() {
        long chatId = 12345L;
        when(message.getChatId()).thenReturn(chatId);

        Sleep sleep = new Sleep();
        sleep.setChatId(chatId);
        sleep.setWentToBed(LocalDateTime.now());
        sleep.setWokeUp(LocalDateTime.now());
        when(sleepHolder.getSleep(chatId)).thenReturn(sleep);

        addSleepMessageStrategy.execute(message);

        verify(sleepHolder, times(1)).getSleep(any(Long.class));
    }

}
