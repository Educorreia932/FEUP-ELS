package pt.up.fe.els2023;

import org.junit.Test;

public class ExternalTest {
    @Test
    public void assignment1() {
        new Parser().parse("test/resources/checkpoint1/config.tablefork");   
    }
}
