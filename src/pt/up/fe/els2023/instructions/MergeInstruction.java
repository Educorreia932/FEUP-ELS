package pt.up.fe.els2023.instructions;

import pt.up.fe.els2023.model.*;
import pt.up.fe.els2023.model.table.*;

import java.util.List;

public class MergeInstruction implements Instruction {
    private final List<String> sources;
    private final String target;

    private final DataSingleton data;

    public MergeInstruction(DataSingleton data, List<String> sources, String target) {
        this.sources = sources;
        this.target = target;
        this.data = data;
    }

    @Override
    public void execute() {
        List<String> headers = data.getTable(sources.get(0)).getHeaders();
        Table output = new Table(target, headers.toArray(new String[0]));

        for (String source : sources) {
            Table table = data.getTable(source);
            
            for (int i = 0; i < table.numRows(); i++) {
                output.addRow(table.getRow(i).toArray());
            }
        }

        data.addTable(output);
    }
}
