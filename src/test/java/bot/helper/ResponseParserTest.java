package bot.helper;

import bot.data_layer.model.User;
import bot.model.MenuItem;
import bot.rest.RestClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ResponseParserTest {

    private static final String CORRECT_MESSAGE = "some {message} from {text}";

    @Mock
    private RestClient restClient;

    @Mock
    private InlineButtonParser inlineButtonParser;

    @Autowired
    @InjectMocks
    private ResponseParser responseParser;

    @BeforeEach()
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetSingleMessage() throws Exception{
        mockGettingObject();
        List<SendMessage> sendMessage = responseParser.getResponseMessage(stubMenuItem(), stubUser());
        assertEquals(1, sendMessage.size());
        assertEquals("some Hi from you", sendMessage.get(0).getText());
    }

    @Test
    void testGetMessageArray()throws Exception {
        mockGettingArray();
        List<SendMessage> sendMessage = responseParser.getResponseMessage(stubMenuItem(), stubUser());
        assertEquals(2, sendMessage.size());
        assertEquals("some Hi from you", sendMessage.get(0).getText());
        assertEquals("some Hello from us", sendMessage.get(1).getText());
    }

    @Test
    void testParse() throws Exception {
        String actual = responseParser.parse(getSimpleJsonNode(), CORRECT_MESSAGE, getFields());
        String expected = "some Hi from you";
        assertEquals(expected, actual);
    }

    private JsonNode getSimpleJsonNode() throws Exception {
        String node = "{\"message\": \"Hi\", \"text\":\"you\", \"test\": \"true\"}";
        return new ObjectMapper().readTree(node);
    }

    private List<String> getFields() {
        return Arrays.asList("{message}", "{text}");
    }

    private void mockGettingObject() throws Exception {
        when(restClient.getForObject(any(String.class), any(HashMap.class))).thenReturn(getSingleObject());
    }

    private void mockGettingArray() throws Exception {
        when(restClient.getForObject(any(String.class), any(HashMap.class))).thenReturn(getArray());
    }

    private JsonNode getSingleObject() throws Exception {
        String node = "{\"message\": \"Hi\", \"text\":\"you\"}";
        return new ObjectMapper().readTree(node);
    }

    private JsonNode getArray() throws Exception {
        String node = "[{\"message\": \"Hi\", \"text\":\"you\"}, {\"message\": \"Hello\", \"text\":\"us\"}]";
        return new ObjectMapper().readTree(node);
    }

    private MenuItem stubMenuItem() {
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Name");
        menuItem.setPath("path/path");
        menuItem.setMessage(CORRECT_MESSAGE);
        menuItem.setSuccessMessage("Success message");
        menuItem.setErrorMessage("Error message");
        return menuItem;
    }

    private User stubUser() {
        User user = new User();
        user.setId(6L);
        user.setChatId(12321L);
        return user;
    }

}