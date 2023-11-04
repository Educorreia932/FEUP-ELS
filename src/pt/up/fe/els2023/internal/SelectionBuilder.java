package pt.up.fe.els2023.internal;

import pt.up.fe.els2023.model.table.ValueType;

public class SelectionBuilder {
    private final TableBuilder tableBuilder;
    private final TableList selections;

    public SelectionBuilder(TableBuilder tableBuilder) {
        this.tableBuilder = tableBuilder;
        this.selections = new TableList();
    }

    public FromSelectionBuilder from(String fromField) {
        return new FromSelectionBuilder(this, fromField, selections, tableBuilder);
    }

    public SelectionBuilder fields(String... fieldNames) {
        selections.add(tableBuilder.table.selectByName(fieldNames));

        return this;
    }

    public SelectionBuilder type(ValueType valueType) {
        selections.add(tableBuilder.table.selectByType(valueType));

        return this;
    }

    public TableBuilder end() {
        tableBuilder.table = selections.merge();

        return tableBuilder;
    }
}
