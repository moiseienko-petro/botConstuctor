package bot.utils;

import java.util.Map;

public abstract class UrlUtil {

    public static String formatUrl(String urlPattern, Map<String, String> values) {
        String result = urlPattern;
        for (String key : values.keySet()) {
            if (urlPattern.contains("{" + key + "}")) {
                result = result.replace("{" + key + "}", values.get(key));
            }
        }
        return result;
    }
}
