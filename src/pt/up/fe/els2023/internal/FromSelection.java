package pt.up.fe.els2023.internal;

import pt.up.fe.els2023.model.table.Table;

import java.util.List;

public class FromSelection {
    private final Selection selection;
    private final String fromField;
    private final List<Table> selections;
    private final Table table;

    public FromSelection(Selection selection, String fromField) {
        this.selection = selection;
        this.fromField = fromField;
        selections = selection.selections;
        table = selection.table;
    }

    public FromSelection fields(String... fieldNames) {
        Table fromTable = table.selectColumnsByName(fromField);

        selections.add(fromTable.selectColumnsByName(fieldNames));

        return this;
    }

    public Selection end() {
        return selection;
    }
}
