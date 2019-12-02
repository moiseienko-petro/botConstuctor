package bot.messageSender;

import bot.Bot;
import bot.data_layer.model.User;
import bot.data_layer.repository.UserRepository;
import bot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class MessageSender {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Bot bot;

    public void sendMessageToUser(Long userId, Message message) {
        User user = userRepository.getOne(userId);
        if(user.getChatId()!=null) {
            SendMessage sendMessage = new SendMessage() // Create a message object object
                    .setChatId(user.getChatId())
                    .setText(message.getText());
            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToAll(Message message) {
        List<User> users = userRepository.findAll();
        for (User user: users) {
            SendMessage sendMessage = new SendMessage() // Create a message object object
                    .setChatId(user.getChatId())
                    .setText(message.getText());
            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
