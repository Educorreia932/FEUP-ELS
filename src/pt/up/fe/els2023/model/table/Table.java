package pt.up.fe.els2023.model.table;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.ListOrderedMap;

public class Table {
    private final ListOrderedMap<String, Column<?>> columns = new ListOrderedMap<>();

    public Table() {

    }

    public static Table fromContents(Map<String, Object> contents) {
        Table table = new Table();

        for (Map.Entry<String, Object> entry : contents.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Array
            if (value instanceof List<?>) {
                Table arrayTable = new Table();
                int i = 0;

                // Iterate rows
                for (Object row : (List<?>) value) {
                    if (row instanceof Map<?, ?>) {
                        Column<Table> column = new Column<>(String.valueOf(i));
                        column.addElement(fromContents((Map<String, Object>) row));

                        arrayTable.addColumn(column);
                    }

                    else {
                        Column<String> column = new Column<>(String.valueOf(i));
                        column.addElement(String.valueOf(row));

                        arrayTable.addColumn(column);
                    }

                    i++;
                }

                table.addColumn(key, Collections.singletonList(arrayTable));
            }

            // Object
            else if (value instanceof Map<?, ?>)
                table.addColumn(key, List.of(fromContents((Map<String, Object>) value)));

                // Atomic value
            else
                table.addColumn(key, List.of(String.valueOf(value)));
        }

        return table;
    }

    public Table(List<String> headers) {
        for (String header : headers) {
            Column<?> column = new Column(header);

            columns.put(column.getHeader(), column);
        }
    }

    public ArrayList<?> getRow(int index) {
        return columns.values().stream()
            .map(column -> column.getElement(index))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<List> getRows() {
        List<List> rows = new ArrayList<>();

        for (int i = 0; i < numRows(); i++)
            rows.add(getRow(i));

        return rows;
    }

    public Column<?> getColumn(String header) {
        return columns.get(header);
    }

    public Column<?> getColumn(int index) {
        return columns.getValue(index);
    }

    public void addRow(List<?> row) {
        if (row.size() != numColumns())
            throw new IllegalArgumentException("Row must have same number of elements as the number of columns.");

        for (int i = 0; i < columns.size(); i++) {
            Column column = getColumn(i);

            column.addElement(row.get(i));
        }
    }

    public void addColumn(Column<?> column) {
        columns.put(column.getHeader(), column);
    }
    
    public void addColumn(String header, List<?> elements) {
        Column<?> column = new Column<>(header, elements);

        columns.put(header, column);
    }

    public void removeRow(int index) {
        columns.forEach((key, column) -> {
            column.removeElement(index);
        });
    }

    public void removeColumn(String key) {
        columns.remove(key);
    }

    public int numRows() {
        return getColumn(0).numElements();
    }

    public int numColumns() {
        return columns.size();
    }

    public List<String> getHeaders() {
        List<String> headers = new ArrayList<>();

        columns.forEach((key, value) -> {
            headers.add(value.getHeader());
        });

        return headers;
    }

    public static Table concat(List<Table> tables) {
        List<String> headers = tables.get(0).getHeaders();
        Table result = new Table(headers);

        for (Table table : tables)
            for (List<?> row : table.getRows())
                result.addRow(row);

        return result;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object == null || getClass() != object.getClass())
            return false;

        Table table = (Table) object;

        // Compare headers
        if (!getHeaders().equals(table.getHeaders()))
            return false;

        // Compare each column
        for (String header : getHeaders()) {
            Column<?> thisColumn = getColumn(header);
            Column<?> otherColumn = table.getColumn(header);

            if (!thisColumn.getElements().equals(otherColumn.getElements()))
                return false;
        }

        return true;
    }
}
