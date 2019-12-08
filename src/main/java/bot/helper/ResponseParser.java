package bot.helper;

import bot.data_layer.model.User;
import bot.model.MenuItem;
import bot.rest.RestClient;
import bot.utils.FieldsUtil;
import bot.utils.PrettyTextUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.*;

@Component
public class ResponseParser {

    @Autowired
    private RestClient restClient;

    @Autowired
    private InlineButtonParser inlineButtonParser;


    public List<SendMessage> getResponseMessage(MenuItem menuItem, User user) throws Exception {

        if (menuItem.getPath() == null || menuItem.getPath().trim().length() == 0) {
            return Collections.singletonList(getMessageWithoutParsing(menuItem, user));
        }

        JsonNode response = getResponse(menuItem, user.getId());
        String message = menuItem.getMessage();
        List<String> fieldPatterns = FieldsUtil.getFieldNamePatternsFromText(message);

        if (fieldPatterns == null) {
            return Collections.singletonList(getMessageWithoutParsing(menuItem, user));
        }

        if (isNodeContainsArray(response)) {
            return getMessagesForArray(response, user, menuItem, fieldPatterns);
        }

        SendMessage sendMessage = createSingleMessage(response, user, menuItem, fieldPatterns);
        return Collections.singletonList(sendMessage);
    }

    private JsonNode getResponse(MenuItem menuItem, Long userId) throws Exception {
        String url = menuItem.getPath();
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("userId", userId.toString());
        return restClient.getForObject(url, urlParams);
    }

    String parse(JsonNode object, String message, List<String> fields) {
        Map<String, String> valueMap = FieldsUtil.createValueMapForFields(object, fields);
        String result = FieldsUtil.replaceFieldPatternWithValues(message, valueMap);
        result = PrettyTextUtil.splitMultiLineText(result);
        return result;
    }

    private List<SendMessage> getMessagesForArray(JsonNode node, User user,
                                                  MenuItem menuItem, List<String> fieldPatterns) {

        Iterator<JsonNode> iterator = node.elements();
        List<SendMessage> sendMessages = new ArrayList<>();
        while (iterator.hasNext()) {
            JsonNode object = iterator.next();
            SendMessage sendMessage = createSingleMessage(object, user, menuItem, fieldPatterns);
            sendMessages.add(sendMessage);
        }
        return sendMessages;
    }

    private boolean isNodeContainsArray(JsonNode node) {
        return node.toString().startsWith("[");
    }

    private SendMessage createSingleMessage(JsonNode node, User user,
                                            MenuItem menuItem, List<String> fieldPatterns) {

        String resultMessage = parse(node, menuItem.getMessage(), fieldPatterns);
        InlineKeyboardMarkup markup = inlineButtonParser.parseButton(menuItem, user, node);
        SendMessage resultSendMessage = new SendMessage()
                .setText(resultMessage)
                .setChatId(user.getChatId());
        if (markup != null) {
            resultSendMessage.setReplyMarkup(markup);
        }
        return resultSendMessage;
    }

    private SendMessage getMessageWithoutParsing(MenuItem menuItem, User user) {
        return new SendMessage()
                .setChatId(user.getChatId())
                .setText(menuItem.getMessage());
    }
}
