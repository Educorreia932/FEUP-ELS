package pt.up.fe.els2023.internal;

import pt.up.fe.els2023.model.table.Table;

import java.util.Collections;
import java.util.List;

public class TableBuilder {
    private final Program program;
    Table table;

    public TableBuilder(Program program, Table table) {
        this.program = program;
        this.table = table;
    }

    public SelectionBuilder select() {
        return new SelectionBuilder(this);
    }

    public TableBuilder concat(String... headers) {
        TableList tableList = TableList.fromArrayTable(table);
        table = tableList.concat(headers);

        return this;
    }

    public TableBuilder max(String field) {
        List<Object> elements = table.getColumn(field).getElements();

        if (!(elements.get(0) instanceof Number))
            throw new RuntimeException("Column is not of numberic type.");

        List<Double> numberList = elements.stream().map(e -> (Double) e).toList();
        int index = numberList.indexOf(Collections.max(numberList));
        
        table = table.slice(index, index + 1);
        
        return this;
    }

    public Program end() {
        program.addTable(table);

        return program;
    }
}
