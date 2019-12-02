package bot.helper;

import bot.configs.BotConfig;
import bot.data_layer.model.User;
import bot.data_layer.repository.UserRepository;
import bot.rest.RestClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserHelper {

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private RestClient restClient;

    @Autowired
    private UserRepository userRepository;

    public User getUser(String phone, Long chatId, Integer telegramId) throws Exception {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("phone", phone);
        JsonNode response = restClient.getForObject(botConfig.getUserApiPath(), urlParams);
        if (response == null) {
            throw new Exception("User not found");
        }

        User user = new User();
        user.setId(response.path("id").asLong());
        user.setFirstName(response.path("firstName").asText());
        user.setLastName(response.path("lastName").asText());
        user.setChatId(chatId);
        user.setPhoneNumber(phone);
        user.setTelegramId(telegramId);

        userRepository.save(user);
        return user;
    }
}
