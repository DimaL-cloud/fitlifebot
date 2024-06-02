package ua.bibusukraine.fitlifebot.telegram.command.strategy;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

public abstract class TelegramMessageStrategy {

    public abstract SendMessage buildSendMessage();

    protected SendMessage buildRequestFieldMessage(Long chatId, String text) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(text);
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        response.setReplyMarkup(replyKeyboardRemove);
        return response;
    }

}
