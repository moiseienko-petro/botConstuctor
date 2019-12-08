package bot.helper;


import bot.data_layer.ActionButtonManager;
import bot.data_layer.model.ActionButton;
import bot.data_layer.model.ActionItem;
import bot.data_layer.model.User;
import bot.model.MenuItem;
import bot.utils.FieldsUtil;
import bot.utils.builders.ActionButtonBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InlineButtonParser {

    private static final String TAG_VALUE_INNER = "'";
    private static final String VALUE_TAG = "value=" + TAG_VALUE_INNER;
    private static final String METHOD_TAG = "method=" + TAG_VALUE_INNER;
    private static final String TAG_OPENER = "<";
    private static final String TAG_CLOSER = ">";
    private static final Pattern FIELD_NAME_PATTERN = Pattern.compile("\\{((?!\\{).)*}");


    @Autowired
    private ActionButtonManager actionButtonManager;

    public InlineKeyboardMarkup parseButton(MenuItem item, User user, JsonNode node) {
        ActionButton actionButton;
        String actionMessage;
        String successMessage = item.getSuccessMessage();
        String errorMessage = item.getErrorMessage();

        if (item.getAction() == null || item.getActionMessage() == null) {
            return null;
        } else {
            if (!item.getActionMessage().contains(TAG_OPENER)) {
                actionMessage = getActionMessageWithoutConditions(item.getActionMessage());
                actionButton = getActionButtonWithoutConditions(item.getAction(), node, user.getId(), actionMessage, successMessage, errorMessage);
            } else {
                actionMessage = getActionMessage(item.getActionMessage(), node, true);
                String oppositeActionMessage = getActionMessage(item.getActionMessage(), node, false);
                actionButton = getActionButton(item.getAction(), node, user.getId(), actionMessage, oppositeActionMessage, successMessage, errorMessage);
            }

            InlineKeyboardMarkup markup = createMarkup(actionButton, actionMessage, user.getTelegramId());
            return markup;
        }
    }

    InlineKeyboardMarkup createMarkup(ActionButton actionButton, String text, Integer telegramId) {
        actionButton.setTelegramId(telegramId);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);

        Long id = actionButtonManager.saveActionButton(actionButton);
        button.setCallbackData(id.toString());
        markup.setKeyboard(Collections.singletonList(Collections.singletonList(button)));
        return markup;
    }

    String getActionMessageWithoutConditions(String message) {
        return getTagValue(message, VALUE_TAG);
    }

    String getActionMessage(String message, JsonNode node, boolean isIfPart) {
        String ifPart = message.substring(0, message.indexOf(TAG_CLOSER) + 1);
        String elsePart = message.substring(message.indexOf(TAG_CLOSER) + 1);

        Matcher matcher = FIELD_NAME_PATTERN.matcher(ifPart);
        String fieldName;
        if (matcher.find()) {
            fieldName = matcher.group().replace("{", StringUtils.EMPTY)
                    .replace("}", StringUtils.EMPTY);
        } else {
            return null;
        }

        String value = node.path(fieldName).asText();
        if (value.equals("true")) {
            return getTagValue(isIfPart ? ifPart : elsePart, VALUE_TAG);
        } else {
            return getTagValue(isIfPart ? elsePart : ifPart, VALUE_TAG);
        }
    }

    ActionButton getActionButtonWithoutConditions(String action, JsonNode node,
                                                          Long userId, String actionMessage,
                                                          String successMessage, String errorMessage) {
        ActionItem actionItem = new ActionItem();
        actionItem.setAction(getAction(action, userId, node));
        actionItem.setMethod(getTagValue(action, METHOD_TAG));
        actionItem.setMessage(actionMessage);

        return new ActionButtonBuilder().setRequest(actionItem)
                .setOppositeRequest(new ActionItem())
                .setSuccessMessage(successMessage)
                .setErrorMessage(errorMessage).build();
    }

    ActionButton getActionButton(String action, JsonNode node,
                                         Long userId, String actionMessage, String oppositeMessage,
                                         String successMessage, String errorMessage) {
        String ifPart = action.substring(0, action.indexOf(TAG_CLOSER)+1);
        String elsePart = action.substring(action.indexOf(TAG_CLOSER) + 1);

        Matcher matcher = FIELD_NAME_PATTERN.matcher(ifPart);
        String fieldName;
        if (matcher.find()) {
            fieldName = matcher.group().replace("{", StringUtils.EMPTY)
                    .replace("}", StringUtils.EMPTY);
        } else {
            return null;
        }

        String value = node.path(fieldName).asText();
        if (value.equals("true")) {
            return createActionButton(node, userId, ifPart, elsePart, actionMessage, oppositeMessage, successMessage, errorMessage);
        } else {
            return createActionButton(node, userId, elsePart, ifPart, actionMessage, oppositeMessage, successMessage, errorMessage);
        }

    }

    ActionButton createActionButton(JsonNode node, Long userId,
                                            String requestPart, String oppositeRequestPart,
                                            String actionMessage, String oppositeMessage,
                                            String successMessage, String errorMessage) {
        ActionItem request = new ActionItem();
        request.setMethod(getTagValue(requestPart, METHOD_TAG));
        request.setAction(getAction(requestPart, userId, node));
        request.setMessage(actionMessage);

        ActionItem oppositeRequest = new ActionItem();
        oppositeRequest.setMethod(getTagValue(oppositeRequestPart, METHOD_TAG));
        oppositeRequest.setAction(getAction(oppositeRequestPart, userId, node));
        oppositeRequest.setMessage(oppositeMessage);
        return new ActionButtonBuilder().setRequest(request)
                .setOppositeRequest(oppositeRequest)
                .setSuccessMessage(successMessage)
                .setErrorMessage(errorMessage).build();
    }

    String getTagValue(String tag, String tagName) {
        int methodTagIndex = tag.indexOf(tagName);
        int startTagValue = methodTagIndex + tagName.length();
        String tagPart = tag.substring(startTagValue);
        int endTagValue = tagPart.indexOf("'");
        return tagPart.substring(0, endTagValue);
    }

    String getAction(String tag, Long userId, JsonNode node) {
        String value = getTagValue(tag, VALUE_TAG);
        Map<String, String> params = getValueMap(node, value, userId);
        return FieldsUtil.replaceFieldPatternWithValues(value, params);
    }

    Map<String, String> getValueMap(JsonNode node, String url, Long userId) {

        List<String> fieldNames = FieldsUtil.getFieldNamePatternsFromText(url);
        Map<String, String> valueMap = FieldsUtil.createValueMapForFields(node, fieldNames);
        valueMap.put("{userId}", userId.toString());
        return valueMap;
    }

}
