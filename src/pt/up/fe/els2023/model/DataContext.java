package pt.up.fe.els2023.model;

import pt.up.fe.els2023.model.table.Table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.map.ListOrderedMap;

public class DataContext {
    private final Table masterTable = new Table();

    public DataContext() {
        masterTable.addColumn("Filename", new ArrayList<String>());
        masterTable.addColumn("Table Name", new ArrayList<String>());
        masterTable.addColumn("Table", new ArrayList<Table>());
    }

    public void addTable(String filename, Table table) {
        masterTable.addRow(List.of(filename, filename, table));
    }
    
    public void removeTable(String name) {
        int i = 0;
        
        for (List<?> entry : masterTable.getRows()) {
            if (entry.get(1).equals(name))
                break;
            
            i++;
        }
        
        masterTable.removeRow(i);
    }
    
    public void replaceTable(String name, Table newTable) {
        removeTable(name);
        addTable(name, newTable);
    }
    
    // TODO: Add method to keep column header in rows
    public List<List> getEntries() {
        return masterTable.getRows();
    }

    public Table getTable(String name) {
        for (List<?> entry : masterTable.getRows()) {
            if (entry.get(1) .equals(name))
                return (Table) entry.get(2);
        }
        
        return null;
    }
}
