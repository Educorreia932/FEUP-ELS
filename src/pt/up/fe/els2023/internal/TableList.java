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

    public Table merge() {
        List<Column<?>> columns = new ArrayList<>();

        for (Table table : tables)
            columns.addAll(table.getColumns());

        return new Table(columns.toArray(Column[]::new));
    }

    public void add(Table table) {
        tables.add(table);
    }

    public Table get(int index) {
        return tables.get(0);
    }
}
