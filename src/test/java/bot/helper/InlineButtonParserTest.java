package bot.helper;

import bot.data_layer.ActionButtonManager;
import bot.data_layer.model.ActionButton;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class InlineButtonParserTest {

    private static final String CORRECT_MESSAGE = "some {message} from {text}";

    private static final String IF_TAG = "<if {test} value='post/{message}/{text}' method='POST'>";
    private static final String ELSE_TAG = "<else value='delete/{message}/{text}' method='DELETE'>";

    private static final String TAG = IF_TAG + ELSE_TAG;

    @Mock
    private ActionButtonManager actionButtonManagerMock;

    @Autowired
    @InjectMocks
    private InlineButtonParser inlineButtonParser;

    @BeforeEach()
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateMarkup() throws Exception {
        mockSaveActionButton();
        ActionButton actionButton = getActionButton();
        InlineKeyboardMarkup markup = inlineButtonParser.createMarkup(actionButton, "Test actionButton", 954);
        assertEquals("8", markup.getKeyboard().get(0).get(0).getCallbackData());
    }

    @Test
    void getActionMessage() throws Exception {
        String actual = inlineButtonParser.getActionMessage(TAG, getSimpleJsonNode(), true);
        String expected = "post/{message}/{text}";
        assertEquals(expected, actual);
    }

    @Test
    void testGetActionButton() throws Exception {
        String actionMessage = "Action";
        String oppositeMessage = "Opposite";
        String successMessage = "Success";
        String errorMessage = "Error";
        ActionButton actual = inlineButtonParser.getActionButton(TAG, getSimpleJsonNode(), 6L, actionMessage, oppositeMessage, successMessage, errorMessage);
        ActionButton expected = getActionButton();
        assertEquals(expected, actual);
    }

    @Test
    void testGetTagValue() {
        String actualValue = inlineButtonParser.getTagValue(IF_TAG, "value='");
        String expectedValue = "post/{message}/{text}";
        String actualMethod = inlineButtonParser.getTagValue(IF_TAG, "method='");
        String expectedMethod = "POST";
        assertEquals(expectedMethod, actualMethod);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void getGetAction() throws Exception {
        String actual = inlineButtonParser.getAction(IF_TAG, 6L, getSimpleJsonNode());
        String expected = "post/Hi/you";
        assertEquals(expected, actual);
    }

    @Test
    void testGetValueMap() throws Exception {
        Map<String, String> actual = inlineButtonParser.getValueMap(getSimpleJsonNode(), CORRECT_MESSAGE, 6L);
        Map<String, String> expected = getValueMapTempFromCorrectMessage();
        assertEquals(expected, actual);
    }

    private void mockSaveActionButton() {
        when(actionButtonManagerMock.saveActionButton(any(ActionButton.class))).thenReturn(8L);
    }

    private Map<String, String> getValueMapTempFromCorrectMessage() {
        Map<String, String> map = new HashMap();
        map.put("{message}", "Hi");
        map.put("{text}", "you");
        map.put("{userId}", "6");
        return map;
    }

    private Map<String, String> getValueMapTemp() {
        Map<String, String> map = new HashMap();
        map.put("{message}", "Hi");
        map.put("{text}", "you");
        map.put("{userId}", "6");
        map.put("{test}", "true");
        return map;
    }

    private JsonNode getSimpleJsonNode() throws Exception {
        String node = "{\"message\": \"Hi\", \"text\":\"you\", \"test\": \"true\"}";
        return new ObjectMapper().readTree(node);
    }

    private ActionButton getActionButton() throws Exception {
        String requestPart = IF_TAG;
        String oppositeRequestPart = ELSE_TAG;
        String actionMessage = "Action";
        String oppositeMessage = "Opposite";
        String successMessage = "Success";
        String errorMessage = "Error";
        ActionButton actionButton = inlineButtonParser.createActionButton(getSimpleJsonNode(), 6L, requestPart,
                oppositeRequestPart, actionMessage, oppositeMessage, successMessage, errorMessage);
        return actionButton;
    }
}