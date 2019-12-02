package bot.utils;

import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang.StringUtils;

public abstract class PrettyTextUtil {

    private static final String EMOJI_TAG = "<emoji";
    private static final String CLOSE_TAG_SIGN = ">";
    private static final String BREAK_LINE_SIGN = "\\|";

    public static String replaceEmojiCodeToEmoji(String text) {
        if (text.contains(EMOJI_TAG)) {
            String emojiPart = text.substring(text.indexOf(EMOJI_TAG)).substring(0, text.indexOf(CLOSE_TAG_SIGN) + 1);
            String emoji = emojiPart.replace(EMOJI_TAG, StringUtils.EMPTY).replace(CLOSE_TAG_SIGN, StringUtils.EMPTY);
            text = text.replace(emojiPart, EmojiParser.parseToUnicode(emoji));
            while (text.contains(EMOJI_TAG)) {
                text = replaceEmojiCodeToEmoji(text);
            }
        }
        return text;
    }

    public static String splitMultiLineText(String text) {
        StringBuilder resultMessage = new StringBuilder();
        String[] lines = text.split(BREAK_LINE_SIGN);
        for (int i = 0; i < lines.length; i++) {
            resultMessage.append(lines[i].trim());
            if (i != lines.length - 1) {
                resultMessage.append("\n");
            }
        }
        return replaceEmojiCodeToEmoji(resultMessage.toString());
    }
}
