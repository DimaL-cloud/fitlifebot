package ua.bibusukraine.fitlifebot.telegram.command.strategy.nutrition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.bibusukraine.fitlifebot.cache.ProductHolder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddDishMessageStrategyTest {

  @Mock
  private ProductHolder productHolder;

  @Mock
  private ApplicationEventPublisher applicationEventPublisher;

  @InjectMocks
  private AddDishMessageStrategy addDishMessageStrategy;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void execute_shouldRequestProductName_whenProductDoesNotExist() {
    Message message = mock(Message.class);
    when(message.getChatId()).thenReturn(123L);
    when(productHolder.getProductByChatId(anyLong())).thenReturn(null);

    addDishMessageStrategy.execute(message);

    verify(applicationEventPublisher, times(1)).publishEvent(any(SendMessage.class));
  }

}