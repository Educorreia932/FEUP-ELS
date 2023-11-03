package pt.up.fe.els2023;

import org.junit.Test;
import pt.up.fe.els2023.model.table.Metadata;

import static pt.up.fe.els2023.internal.Program.*;

public class InternalTest {
    @Test
    public void test() {
        program()
            .load("resources/checkpoint2/data/vitis-report.xml")
                .select()
                    .fields(
                        "profile.AreaEstimates.Resources", 
                        Metadata.FOLDER.toString()
                    )
                .end()
            .end()
        .save("out.csv");
    }
}
