package pt.up.fe.els2023;

import org.junit.Test;
import pt.up.fe.els2023.internal.Aggregation;
import pt.up.fe.els2023.model.table.Metadata;
import pt.up.fe.els2023.model.table.ValueType;

import static pt.up.fe.els2023.internal.TableInteraction.*;

public class InternalTest {
    @Test 
    public void assignment1() {
        // Load data
        load("checkpoint1/data/*.yaml")

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
            .save("Assignment 2.csv");
    }

    @Test
    public void assignment3() {
        merge(
            // Table 1
            load("checkpoint3/data/**/analysis.yaml")
                .select()
                    .fields(Metadata.FOLDER.toString())

                    .from("total.results")
                        .fields("dynamic")
                    .end()
                .end()

                .rename(Metadata.FOLDER.toString(), "Folder")
                .rename("iterations", "Iterations (Dynamic)")
                .rename("calls", "Calls (Dynamic)"),

            // Table 2
            load("checkpoint3/data/**/analysis.xml")
                .select()
                    .from("root.total.results")
                        .fields("static")
                    .end()
                .end()

                .rename("nodes", "Nodes (Static)")
                .rename("functions", "Functions (Static)"),

            // Table 3
            load("checkpoint3/data/**/profiling.json")
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

                .unstack()

                .rename("name", "Name")
                .rename("time%", "%")
        )
            .unravel()
            .aggregate(Aggregation.SUM, Aggregation.AVERAGE)
            .save("Assignment 3.csv");
    }
}
