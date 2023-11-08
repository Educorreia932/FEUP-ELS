package pt.up.fe.els2023.instructions;

import pt.up.fe.els2023.model.*;
import pt.up.fe.els2023.model.table.*;

import java.util.List;

public class MergeInstruction implements Instruction {
    private final List<String> sources;
    private final String target;

    private final DataContext data;

    public MergeInstruction(DataContext data, List<String> sources, String target) {
        this.sources = sources;
        this.target = target;
        this.data = data;
    }

    @Override
    public void execute() {
        List<Table> tables = sources.stream().map(data::getTable).toList();

        Table output = Table.concat(tables.toArray(Table[]::new));

        data.addTable(target, output);
    }
}
