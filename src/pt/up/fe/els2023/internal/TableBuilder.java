package pt.up.fe.els2023.internal;

import pt.up.fe.els2023.model.table.Table;

public class TableBuilder {
    private final Program program;
    private Table table;
    
    public TableBuilder(Program program, Table table) {
        this.program = program;
        this.table = table;
    }
    
    public TableBuilder selectByName(String fieldName) {
        this.table = table.selectByName(fieldName);
        
        return this;
    }

    public Program end() {
        program.addTable(table);
        
        return program;
    }
}
