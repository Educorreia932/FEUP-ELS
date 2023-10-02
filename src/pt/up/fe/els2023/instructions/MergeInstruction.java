package pt.up.fe.els2023.instructions;

import pt.up.fe.els2023.model.*;
import pt.up.fe.els2023.model.table.*;

import java.util.List;
import javafx.util.Pair;

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

        Table output = new Table();
        for(String header: data.getTable(sources.get(0)).getHeaders()){
            output.addColumn(header);
        }

        for(String source: sources){
            for(int i = 0; i<data.getTable(source).numRows(); i++){
                output.addRow(data.getTable(source).getRow(i));
            }
        }

        output.setName(target);
        data.addTable(target, output);

    }
}
