package bot.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FieldsUtil {

    private static String FIELD_BRACKETS_REGEX = "\\{((?!\\{).)*}";
    private static String FIELD_OPEN_BRACKET = "{";
    private static String FIELD_CLOSE_BRACKET = "}";
    private static String FIELD_NAME_SPLITTER_REGEX = "\\.";


    public static List<String> getFieldNamePatternsFromText(String message) {
        List<String> names = null;
        Pattern pattern = Pattern.compile(FIELD_BRACKETS_REGEX);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            names = new ArrayList<>();
            names.add(matcher.group());

            while (matcher.find()) {
                names.add(matcher.group());
            }
        }
        return names;
    }

    public static Map<String, String> createValueMapForFields(JsonNode node, List<String> fieldNames) {
        Map<String, String> valueMap = new HashMap<>();
        for (String name : fieldNames) {
            String unbracersName = unbraceFieldName(name);
            String[] pathToField = unbracersName.split(FIELD_NAME_SPLITTER_REGEX);
            JsonNode value = node;
            for (String field : pathToField) {
                value = value.path(field);
            }
            String result = value.asText();
            valueMap.put(name, result);
        }
        return valueMap;
    }

    public static String replaceFieldPatternWithValues(String message, Map<String, String> valueMap) {
        String result = message;
        for (String key : valueMap.keySet()) {
            if (message.contains(key)) {
                result = result.replace(key, valueMap.get(key));
            }
        }

        return result;
    }

    private static String unbraceFieldName(String text) {
        String unbracersName = text.replace(FIELD_OPEN_BRACKET, StringUtils.EMPTY)
                .replace(FIELD_CLOSE_BRACKET, StringUtils.EMPTY);
        return unbracersName;
    }
}
