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
            load("checkpoint1/data/decision_tree_1.yaml"),
            load("checkpoint1/data/decision_tree_2.yaml"),
            load("checkpoint1/data/decision_tree_3.yaml")
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
            load("checkpoint2/data/vitis-report.xml")
                .select()
                    .fields(Metadata.FOLDER.toString())
                    .from("profile.AreaEstimates")
                        .fields("Resources")
                    .end()
                .end(),

            // Table 2
            load("checkpoint2/data/decision_tree.yaml")
                .select()
                    .type(ValueType.TABLE)
                    .fields("params")
                .end(),

            // Table 3
            load("checkpoint2/data/profiling.json")
                .select()
                    .fields("functions")
                .end()
            
                .unstack()

                .select()
                    .fields("name", "time%")
                .end()
            
                .max("time%")
        )
        .rename(Metadata.FOLDER.toString(), "Folder")
        .save("Assignment 2.html");
    }

    @Test
    public void assignment3() {
        TableInteraction analysisYAML = load("checkpoint3/data/**/analysis.yaml")
            .select()
                .from("total.results")
                    .fields("dynamic")
                .end()

                .fields(Metadata.FOLDER.toString())
            .end()

            // TODO: Rename columns

            .rename(Metadata.FOLDER.toString(), "Folder");

        TableInteraction analysisXML = load("checkpoint3/data/**/analysis.xml")
            .select()
                .from("root.total.results")
                    .fields("static")
                .end()
            .end();
        
        TableInteraction profilingJSON = load("checkpoint3/data/**/profiling.json")
            .select()
                .fields("functions")
            .end()

            .unstack()

            .forEach(x ->
                x
                    .slice(0, 3)
                    .select()
                        .fields("name", "time%")
                    .end()
                    .stack()
            )
            
            .unstack();

        // TODO: Select columns from sub-tables and rename them 

        merge(
            // analysisYAML,
            // analysisXML,
            profilingJSON
        )
            // TODO: Rows with sum and average values
            .save("Assignment 3.csv");
    }
}
