package ua.bibusukraine.fitlifebot.telegram.command.strategy;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class UnrecognisedMessageStrategy extends TelegramMessageStrategy {

    private final Message message;

    public UnrecognisedMessageStrategy(Message message) {
        this.message = message;
    }

    @Override
    public SendMessage buildSendMessage() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Нерозпізнана команда");
        return sendMessage;
    }

}
