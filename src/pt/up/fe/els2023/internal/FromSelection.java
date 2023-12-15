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
        Table fromTable = table;

        String[] parts = fromField.split("\\.");

        for (String part : parts)
            fromTable = fromTable.extract(part);

        for (String fieldName : fieldNames) {
            if (!fromTable.hasColumn(fieldName))
                throw new IllegalArgumentException("Table does not have column " + fieldName);

            selections.add(fromTable.extract(fieldName));
        }

        return this;
    }

    public Selection end() {
        return selection;
    }
}
