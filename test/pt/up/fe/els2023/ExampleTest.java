package pt.up.fe.els2023;

import static org.junit.Assert.*;

import org.junit.Test;
import pt.up.fe.specs.util.SpecsIo;


public class ExampleTest {
    @Test
    public void parseFile() {
        Parser parser = new Parser();
        
        parser.parse("resources/decision_tree_1.yaml");
    }
}