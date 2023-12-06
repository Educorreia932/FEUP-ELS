package pt.up.fe.els2023.internal;

import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.ValueType;
import pt.up.fe.els2023.model.table.column.Column;

import java.util.ArrayList;
import java.util.List;

import static pt.up.fe.els2023.model.table.Table.merge;

public class Selection {
    final Table table;
    final List<Table> selections;

    public Selection(Table table) {
        this.table = table;
        this.selections = new ArrayList<>();
    }

    public FromSelection from(String fromField) {
        return new FromSelection(this, fromField);
    }

    public Selection fields(String... fieldNames) {
        for (String fieldName : fieldNames) {
            if (!table.hasColumn(fieldName))
                throw new IllegalArgumentException("Table does not have column " + fieldName);

            selections.add(table.extract(fieldName));
        }

        return this;
    }

    public Selection type(ValueType valueType) {
        for (Column column : table.getColumns())
            if (column.getType() == valueType)
                selections.add(table.extract(column.getHeader()));

        return this;
    }

    public Table end() {
        return merge(selections.toArray(Table[]::new));
    }
}
