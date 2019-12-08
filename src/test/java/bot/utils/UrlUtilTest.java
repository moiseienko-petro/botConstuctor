package bot.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UrlUtilTest {

    @Test
    public void formatUrl() {
        String urlTemplate= "/user/{userId}/abcde/{text}";
        Map<String, String> map = new HashMap<>();
        map.put("userId", "6");
        map.put("text","fgh");
        String actual = UrlUtil.formatUrl(urlTemplate, map);
        String expected = "/user/6/abcde/fgh";
        assertEquals(expected, actual);
    }

    @Test
    public void formatWrongUrl() {
        String urlTemplate= "/user/{userId/abcde/text}";
        Map<String, String> map = new HashMap<>();
        map.put("userId", "6");
        map.put("text","fgh");
        String actual = UrlUtil.formatUrl(urlTemplate, map);
        assertEquals(urlTemplate, actual);
    }
}