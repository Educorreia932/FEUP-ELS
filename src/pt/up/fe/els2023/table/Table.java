package pt.up.fe.els2023.table;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Table {
    private final LinkedHashMap<String, Column<?>> columns = new LinkedHashMap<>();

    public Table() {
    }

    List<Object> getRow(int index) {
        return columns.values().stream()
            .map(column -> column.getElement(index))
            .collect(Collectors.toList());
    }

    Column<?> getColumn(String key) {
        return columns.get(key);
    }

    void addRow(List<?> row) {
        columns.forEach((key, column) -> {
            column.addElement(row);
        });
    }

    void addColumn(Column<?> column) {
        columns.put(column.getKey(), column);
    }

    void removeRow(int index) {
        // TODO:
    }

    void removeColumn(String key) {
        columns.remove(key);
    }

    void renameColumn() {
        // TODO:
    }
}
