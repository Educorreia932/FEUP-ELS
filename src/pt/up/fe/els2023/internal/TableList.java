package pt.up.fe.els2023.internal;

import pt.up.fe.els2023.model.table.Column;
import pt.up.fe.els2023.model.table.Table;

import java.util.ArrayList;
import java.util.List;

public class TableList {
    private final List<Table> tables;

    public TableList() {
        tables = new ArrayList<>();
    }
    
    public static TableList fromArrayTable(Table table) {
        TableList tableList = new TableList();
        
        for (Column<?> column : table.getColumns()) {
            Table subTable = (Table) column.getElements().get(0);
            
            tableList.add(subTable);
        }
        
        return tableList;
    }

    public Table merge() {
        List<Column<?>> columns = new ArrayList<>();

        for (Table table : tables)
            columns.addAll(table.getColumns());

        return new Table(columns.toArray(Column[]::new));
    }

    public Table concat(String... headers) {
        if (headers.length == 0)
            headers = tables.get(0).getHeaders().toArray(String[]::new);
        
        Table concatenatedTable = new Table(headers);

        for (Table table : tables) {
            List<Object> row = new ArrayList<>();
            
            for (String header : headers) {
                Column<?> column = table.getColumn(header);
                
                if (column == null)
                    row.add(null);
                    
                else
                    row.add(column.getElements().get(0));
            }

            concatenatedTable.addRow(row);
        }
            
        return concatenatedTable;
    }

    public void add(Table table) {
        tables.add(table);
    }
}
