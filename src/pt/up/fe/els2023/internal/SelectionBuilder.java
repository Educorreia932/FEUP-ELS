package pt.up.fe.els2023.internal;

public class SelectionBuilder {
    private final TableBuilder tableBuilder;
    private final TableList selections;

    public SelectionBuilder(TableBuilder tableBuilder) {
        this.tableBuilder = tableBuilder;
        this.selections = new TableList();
    }

    public SelectionBuilder fields(String... fieldNames) {
        selections.addTable(tableBuilder.table.selectByName(fieldNames));

        return this;
    }

    public TableBuilder end() {
        tableBuilder.table = selections.merge();

        return tableBuilder;
    }
}
