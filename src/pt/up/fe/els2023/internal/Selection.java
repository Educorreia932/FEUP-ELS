package pt.up.fe.els2023.internal;

import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.ValueType;

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
        selections.add(table.selectColumnsByName(fieldNames));

        return this;
    }

    public Selection type(ValueType valueType) {
        selections.add(table.selectColumnsByType(valueType));

        return this;
    }

    public Table end() {
        return merge(selections.toArray(Table[]::new));
    }
}
