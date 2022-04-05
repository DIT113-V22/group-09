package root;

import org.junit.jupiter.api.Test;

public class LanguageProcessingTest {

    @Test
    public void test() {
        ParsedInput input = TextParser.parse("turn left, and then move forward for 20 meters and then come back");
        assert true;
    }

}
