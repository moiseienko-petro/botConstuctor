package bot.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrettyTextUtilTest {
    private static final String TEXT = "Some | text | message";

    @Test
    public void splitMultiLineText() {
        String actual = PrettyTextUtil.splitMultiLineText(TEXT);
        String expected = "Some" + "\n" + "text" + "\n" + "message";
        assertEquals(expected, actual);
    }
}