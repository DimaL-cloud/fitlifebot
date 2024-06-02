package ua.bibusukraine.fitlifebot.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

public class TelegramMessageUtil {

    public static SendMessage buildSendMessage(Long chatId, String text) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(text);
        return response;
    }

    public static SendMessage buildRequestFieldMessage(Long chatId, String text) {
        SendMessage response = buildSendMessage(chatId, text);
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        response.setReplyMarkup(replyKeyboardRemove);
        return response;
    }

}
