package bot.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class FieldsUtilTest {

    private static final String CORRECT_MESSAGE = "some {message} from {text} find {fields}";
    private static final String WRONG_MESSAGE = "wrong message} {string";

    @Test
    public void getFieldNamePatternsFromText() {
        List<String> actual = FieldsUtil.getFieldNamePatternsFromText(CORRECT_MESSAGE);
        List<String> expected = getFields();
        assertEquals(expected, actual);
    }

    @Test
    public void getFieldNamePatternsFromWrongText(){
        List<String> fields = FieldsUtil.getFieldNamePatternsFromText(WRONG_MESSAGE);
        assertNull(fields);
    }

    @Test
    public void createValueMapForFields() throws Exception{
        JsonNode node = getSimpleJsonNode();
        List<String> fields = getFields();

        Map<String, String> actual = FieldsUtil.createValueMapForFields(node, fields);
        Map<String, String> expected = getValueMap();
        assertEquals(expected, actual);
    }

    @Test
    public void replaceFieldPatternWithValues() {
        String text = CORRECT_MESSAGE;
        Map<String, String> valueMap = getValueMap();
        String actual = FieldsUtil.replaceFieldPatternWithValues(text, valueMap);
        String expected = "some Hi from you find there";
        assertEquals(expected, actual);
    }

    private List<String> getFields() {
        return Arrays.asList("{message}", "{text}", "{fields}");
    }

    private JsonNode getSimpleJsonNode() throws Exception {
        String node = "{\"message\": \"Hi\", \"text\":\"you\", \"fields\": \"there\"}";
        return new ObjectMapper().readTree(node);
    }

    private Map<String, String> getValueMap() {
        Map<String, String> map = new HashMap();
        map.put("{message}", "Hi");
        map.put("{text}", "you");
        map.put("{fields}", "there");
        return map;
    }
}