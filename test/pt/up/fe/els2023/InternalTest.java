package pt.up.fe.els2023;

import org.junit.Test;

import static pt.up.fe.els2023.internal.Program.*;

public class InternalTest {
    @Test
    public void test() {
        program()
            .load("resources/checkpoint1/data/decision_tree_1.yaml")
                .selectByName("params")
            .end()
        .save("out.csv");
    }
}
