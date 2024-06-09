package ua.bibusukraine.fitlifebot.model;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

public class RequestFieldMessage extends SendMessage {

    public RequestFieldMessage(String chatId, String text) {
        super(chatId, text);
        ReplyKeyboardRemove replyKeyboardRemove = createReplyKeyboardRemove();
        setReplyMarkup(replyKeyboardRemove);
    }


    private ReplyKeyboardRemove createReplyKeyboardRemove() {
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        return replyKeyboardRemove;
    }

}