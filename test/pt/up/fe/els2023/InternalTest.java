package pt.up.fe.els2023;

import org.junit.Test;
import pt.up.fe.els2023.model.table.Metadata;
import pt.up.fe.els2023.model.table.ValueType;

import static pt.up.fe.els2023.internal.Program.*;

public class InternalTest {
    @Test
    public void test() {
        program()
            .withFolder("resources/checkpoint2/data/")
            
            // Table 1
            .load("vitis-report.xml")
                .select()
                    .fields(
                        "profile.AreaEstimates.Resources", 
                        Metadata.FOLDER.toString()
                    )
                .end()
            .end()

            // Table 2
            .load("decision_tree.yaml")
                .select()
                    .type(ValueType.TERMINAL)
                .end()
            .end()
            
            // Table 3
            .load("profiling.json")
                .select()
                    .fields("functions")
                .end()
            
                .concat("name", "time%")
            
                .max("time%")
            .end()
        .merge()
        .save("Assignment 2.csv");
    }
}
