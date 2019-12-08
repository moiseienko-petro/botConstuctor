package bot.helper;

import bot.configs.BotConfig;
import bot.data_layer.model.User;
import bot.data_layer.repository.UserRepository;
import bot.rest.RestClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserHelperTest {

    @Mock
    private BotConfig botConfig;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestClient restClient;

    @Autowired
    @InjectMocks
    private UserHelper userHelper;

    @BeforeEach()
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetUser() throws Exception {
        mockOperations();
        User actual = userHelper.getUser("1234567", 12345L, 6789);
        User expected = stubUser("1234567", 12345L, 6789);
        assertEquals(expected, actual);
    }

    private void mockOperations() throws Exception {
        when(botConfig.getUserApiPath()).thenReturn("");
        when(userRepository.save(any(User.class))).thenReturn(null);
        when(restClient.getForObject(any(String.class), any(HashMap.class))).thenReturn(stubUserNode());

    }

    private JsonNode stubUserNode() throws Exception {
        String node = "{\"id\": \"2\", \"firstName\":\"First\", \"lastName\": \"Last\"}";
        return new ObjectMapper().readTree(node);
    }

    private User stubUser(String phone, Long chatId, Integer telegramId) throws Exception {
        JsonNode userNode = stubUserNode();
        User user = new ObjectMapper().treeToValue(userNode, User.class);
        user.setPhoneNumber(phone);
        user.setChatId(chatId);
        user.setTelegramId(telegramId);
        return user;
    }
}