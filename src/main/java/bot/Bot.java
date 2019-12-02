package bot;

import bot.configs.BotConfig;
import bot.helper.AnswerHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {

    @Autowired
    private AnswerHelper answerHelper;

    @Autowired
    private BotConfig botConfig;

    final static Logger LOG = Logger.getLogger(Bot.class);


    //example answer TEXT
    public void sendTextMsg(Message msg, String text) {
        try {
            execute(new SendMessage().setChatId(msg.getChatId()).setText(text));
        } catch (Exception e) {
            LOG.error("Can't send Text message: ", e);
        }
    }

    //example answer GIF
    public void sendGIFMsg(Message msg, String gif) {
        try {
            execute(new SendDocument().setChatId(msg.getChatId()).setDocument(gif));
        } catch (Exception e) {
            LOG.error("Can't send GIF message: ", e);
        }
    }

    //example answer IMAGE
    public void sendImageMsg(Message msg, String image) {
        try {
            execute(new SendPhoto().setChatId(msg.getChatId()).setPhoto(image));
        } catch (Exception e) {
            LOG.error("Can't send Photo message: ", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        List<BotApiMethod> messages = null;
        try {
            messages = answerHelper.getAnswer(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (BotApiMethod message : messages) {
                execute(message);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }
}