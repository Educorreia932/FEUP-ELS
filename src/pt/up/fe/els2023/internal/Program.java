package pt.up.fe.els2023.internal;

import pt.up.fe.els2023.model.table.Table;

import java.io.File;

public class Program {
    private final TableList tables;

    private Program() {
        tables = new TableList();
    }
    
    public static Program program() {
        return new Program();
    }
    
    void addTable(Table table) {
        tables.add(table);
    }

    public TableBuilder load(String path) {
        File file = new File(path);
        Table loadedTable = Table.fromFile(file);
        
        return new TableBuilder(this, loadedTable);
    }

    public Table merge() {
        return tables.merge();
    }
}
