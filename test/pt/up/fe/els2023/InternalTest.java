package pt.up.fe.els2023;

import org.junit.Test;
import pt.up.fe.els2023.model.table.Metadata;
import pt.up.fe.els2023.model.table.ValueType;

import static pt.up.fe.els2023.model.table.Table.*;

public class InternalTest {
    @Test 
    public void assignment1() {
        concat(
            // Import tables
            load("resources/checkpoint1/data/decision_tree_1.yaml"),
            load("resources/checkpoint1/data/decision_tree_2.yaml"),
            load("resources/checkpoint1/data/decision_tree_3.yaml")
        ) // Join tables
            
        // Apply transformations
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
            
        // Export result
        .save("Assignment 1.csv");
    }
    
    @Test
    public void assignment2() {
        merge(
            // Table 1
            load("resources/checkpoint2/data/vitis-report.xml")
                .select()
                    .fields(
                        Metadata.FOLDER.toString(),
                        "profile.AreaEstimates.Resources"
                    )
                .end(),

            // Table 2
            load("resources/checkpoint2/data/decision_tree.yaml")
                .select()
                    .type(ValueType.TERMINAL)
                    .fields("params")
                .end(),

            // Table 3
            load("resources/checkpoint2/data/profiling.json")
                .select()
                    .fields("functions")
                .end()
            
                .unflatten()

                .select()
                    .fields("name", "time%")
                .end()
            
                .max("time%")
        )
        .save("Assignment 2.html");
    }
}
