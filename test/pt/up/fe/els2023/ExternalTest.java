package pt.up.fe.els2023;

import org.junit.Test;

public class ExternalTest {
    Parser parser = new Parser();

    @Test
    public void assignment1() {
        parser.parse("test/resources/checkpoint1/config.tablefork");
    }

    @Test
    public void assignment2() {
        parser.parse("test/resources/checkpoint2/config.tablefork");
    }

    @Test
    public void assignment3() {
        parser.parse("test/resources/checkpoint3/config.tablefork");
    }
}
