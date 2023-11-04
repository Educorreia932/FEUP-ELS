package pt.up.fe.els2023;

import org.junit.Test;
import pt.up.fe.els2023.model.table.Metadata;
import pt.up.fe.els2023.model.table.ValueType;

import static pt.up.fe.els2023.internal.Program.*;

public class InternalTest {
    @Test 
    public void assignment1() {
        program()
            .withFolder("resources/checkpoint1/data/")
            
            .load("decision_tree_1.yaml")
                .select()
                    .fields(Metadata.FILENAME.toString())
            
                    .from("params")
                        .fields("criterion", "splitter", "ccp_alpha", "min_samples_split")
                    .end()
                .end()

                .rename(Metadata.FILENAME.toString(), "File")
                .rename("criterion", "Criterion")
                .rename("splitter", "Splitter")
                .rename("ccp_alpha", "CCP Alpha")
                .rename("min_samples_split", "Min Samples Split")
            .end()
            
            .concat()
        .save("Assignment 1.csv");
    }
    
    @Test
    public void assignment2() {
        program()
            .withFolder("resources/checkpoint2/data/")
            
            // Table 1
            .load("vitis-report.xml")
                .select()
                    .fields(
                        Metadata.FOLDER.toString(),
                        "profile.AreaEstimates.Resources"
                    )
                .end()
            .end()

            // Table 2
            .load("decision_tree.yaml")
                .select()
                    .type(ValueType.TERMINAL)
                    .fields("params")
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
        .save("Assignment 2.html");
    }
}
