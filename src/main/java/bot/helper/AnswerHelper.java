package bot.helper;

import bot.configs.BotConfig;
import bot.data_layer.ActionButtonManager;
import bot.data_layer.model.ActionButton;
import bot.data_layer.model.User;
import bot.data_layer.repository.UserRepository;
import bot.model.MenuItem;
import bot.rest.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import com.vdurmont.emoji.EmojiParser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class AnswerHelper {

    private static final String SHARE_NUMBER_EMOJI  = EmojiParser.parseToUnicode(":phone:");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuHelper menuHelper;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private ResponseParser responseParser;

    @Autowired
    private ActionButtonManager actionButtonManager;

    @Autowired
    private RestClient restClient;

    public List<BotApiMethod> getAnswer(Update update) throws Exception {
        List<BotApiMethod> sendMessages = new ArrayList<>();
        if (update.hasCallbackQuery()) {
            return processCallbackQuery(update);
        } else {
            User user = getUserByTelegramId(update.getMessage().getFrom().getId());
            Contact contact = update.getMessage().getContact();
            if (user == null && contact == null) {
                sendMessages.add(getRequestContactMessage(update));
            } else if (contact != null) {
                try {
                    saveUser(update);
                } catch (Exception e) {
                    return Arrays.asList(new SendMessage() // Create a message object object
                            .setChatId(update.getMessage().getChatId())
                            .setText(botConfig.getUserNotFoundMessage())
                            .setReplyMarkup(null));
                }
                sendMessages.addAll(getMenuMessage(update, true));
            } else {
                sendMessages.addAll(getMenuMessage(update, false));
            }

            return sendMessages;
        }
    }

    private ReplyKeyboardMarkup getKeyboard(MenuItem menuItem, boolean isRoot) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        markup.setResizeKeyboard(true);
        List<MenuItem> items = menuItem.getMenuItems();
        if (items != null) {
            int buttonsCount = items.size();
            KeyboardRow row = new KeyboardRow();
            for (int i = 0; i < buttonsCount; i++) {
                KeyboardButton button = new KeyboardButton(items.get(i).getName());
                if (row.size() < 2) {
                    row.add(button);
                }
                if (row.size() == 2) {
                    rows.add(row);
                    row = new KeyboardRow();
                }
            }
            if (row.size() > 0) {
                rows.add(row);
            }
        }
        if (!isRoot) {
            addBackButtonRow(rows, menuItem.getName());
        }
        markup.setKeyboard(rows);
        return markup;
    }

    private void addBackButtonRow(List<KeyboardRow> rows, String menuName) {
        KeyboardRow backButtonRow = getBackButtonRow(menuName);
        if (rows == null) {
            rows = new ArrayList<>();
        }
        rows.add(backButtonRow);
    }

    private List<SendMessage> getMenuMessage(Update update, boolean isFirst) throws Exception {
        List<SendMessage> messages = new ArrayList<>();
        MenuItem menuItem;
        String text = update.getMessage().getText();
        if (isBackButtonPushed(text)) {
            String menuName = getMenuItemNameFromBackButton(text);
            if (menuName == null) {
                menuItem = menuHelper.getMenuItem(text, true, true);
                isFirst = true;
            } else {
                menuItem = menuHelper.getMenuItem(menuName, false, false);
            }
        } else {
            menuItem = menuHelper.getMenuItem(text, isFirst, false);
        }
        if (menuItem == null) {
            messages.add(new SendMessage() // Create a message object object
                    .setChatId(update.getMessage().getChatId())
                    .setText("Smth went wrong"));
        } else {
            User user = userRepository.getUserByTelegramId(update.getMessage().getFrom().getId());
            ReplyKeyboardMarkup markup = getKeyboard(menuItem, isFirst);
            List<SendMessage> resultMessages = new ArrayList<>();
            messages = responseParser.getResponseMessage(menuItem, user);
            if (menuItem.getText() == null || menuItem.getText().isEmpty()) {
                for (SendMessage message : messages) {
                    if (message.getReplyMarkup() == null) {
                        message.setReplyMarkup(markup);
                        break;
                    }
                }
                resultMessages = messages;
            } else {
                resultMessages.add(new SendMessage()
                        .setChatId(user.getChatId())
                        .setText(menuItem.getText())
                        .setReplyMarkup(markup));
                resultMessages.addAll(messages);
            }
            return resultMessages;

        }
        return messages;
    }

    private SendMessage getRequestContactMessage(Update update) throws Exception {
        long chat_id = update.getMessage().getChatId();
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton("Share your number"+SHARE_NUMBER_EMOJI);
        button.setRequestContact(true);
        row.add(button);
        markup.setKeyboard(Arrays.asList(row));

        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chat_id)
                .setText("You are new user. Share your contact please")
                .setReplyMarkup(markup);
        return message;
    }

    private User getUserByTelegramId(Integer id) {
        return userRepository.getUserByTelegramId(id);
    }

    private void saveUser(Update update) throws Exception {
        Contact contact = update.getMessage().getContact();
        User user = userHelper.getUser(contact.getPhoneNumber(), update.getMessage().getChatId()
                , update.getMessage().getFrom().getId());
    }

    private KeyboardRow getBackButtonRow(String childLevelName) {
        MenuItem parenLevelItem = menuHelper.getParentLevel(childLevelName);
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        if (parenLevelItem == null) {
            keyboardButton.setText("Back");
        } else {
            keyboardButton.setText("Back to " + parenLevelItem.getName());
        }
        keyboardRow.add(keyboardButton);
        return keyboardRow;
    }

    private boolean isBackButtonPushed(String text) {
        return text != null && (text.equals("Back") || text.contains("Back to "));
    }

    private String getMenuItemNameFromBackButton(String fullText) {
        if (fullText.equals("Back")) {
            return null;
        } else {
            return fullText.replace("Back to ", "");
        }
    }

    private List<BotApiMethod> processCallbackQuery(Update update) {
        List<BotApiMethod> response = new ArrayList<>();
        Long id = new Long(update.getCallbackQuery().getData());
        ActionButton button = actionButtonManager.getActionButton(id);
        if (call(button)) {
            if (button.getOppositeRequest() != null && button.getOppositeRequest().getAction() != null) {
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setCallbackData(id.toString());
                inlineKeyboardButton.setText(button.getOppositeRequest().getMessage());
                markup.setKeyboard(Collections.singletonList(Collections.singletonList(inlineKeyboardButton)));
                actionButtonManager.replaceRequestInButton(id);
                response.add(new EditMessageReplyMarkup().
                        setChatId(update.getCallbackQuery().getMessage().getChatId()).
                        setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                        .setReplyMarkup(markup));
            }
            response.add(new SendMessage()
                    .setChatId(update.getCallbackQuery().getMessage().getChatId())
                    .setText(button.getSuccessMessage()));
        } else {
            response.add(new SendMessage()
                    .setChatId(update.getCallbackQuery().getMessage().getChatId())
                    .setText(button.getErrorMessage()));
        }
        return response;
    }

    private boolean call(ActionButton actionButton) {
        try {
            restClient.executeForStatus(actionButton.getRequest().getAction(), actionButton.getRequest().getMethod());
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
