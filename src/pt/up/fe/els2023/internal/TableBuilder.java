package pt.up.fe.els2023.internal;

import pt.up.fe.els2023.model.table.Table;

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

    public Program end() {
        program.addTable(table);
        
        return program;
    }
}
