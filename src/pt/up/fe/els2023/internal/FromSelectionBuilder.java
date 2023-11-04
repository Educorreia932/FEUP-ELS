package pt.up.fe.els2023.internal;

import pt.up.fe.els2023.model.table.Table;

public class FromSelectionBuilder {
    private final SelectionBuilder selectionBuilder;
    private final String fromField;
    private final TableList selections;
    private final TableBuilder tableBuilder;

    public FromSelectionBuilder(SelectionBuilder selectionBuilder, String fromField, TableList selections, TableBuilder tableBuilder) {
        this.selectionBuilder = selectionBuilder;
        this.fromField = fromField;
        this.selections = selections;
        this.tableBuilder = tableBuilder;
    }

    public FromSelectionBuilder fields(String... fieldNames) {
        Table fromTable = tableBuilder.table.selectByName(fromField);
        
        selections.add(fromTable.selectByName(fieldNames));

        return this;
    }

    public SelectionBuilder end() {
        return selectionBuilder;
    }
}
