package pt.up.fe.els2023.internal;

import pt.up.fe.els2023.model.table.Table;

import java.io.File;

public class Program {
    private final TableList tables;
    private String folderName;

    private Program() {
        tables = new TableList();
        folderName = "";
    }
    
    public static Program program() {
        return new Program();
    }
    
    public Program withFolder(String folderName) {
        this.folderName = folderName;
        
        return this;
    }

    public TableBuilder load(String path) {
        File file = new File(folderName + path);
        Table loadedTable = Table.fromFile(file);
        
        return new TableBuilder(this, loadedTable);
    }

    public Table merge() {
        return tables.merge();
    }

    void addTable(Table table) {
        tables.add(table);
    }
}
