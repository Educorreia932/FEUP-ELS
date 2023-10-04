package pt.up.fe.els2023.model.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.map.ListOrderedMap;

public class Table {
    private final ListOrderedMap<String, Column> columns = new ListOrderedMap<>();

    public Table() {
        
    }
    
    public Table(List<String> headers) {
        for (String header : headers) {
            Column column = new Column(header);

            columns.put(column.getHeader(), column);
        }
    }

    public List<String> getRow(int index) {
        return columns.values().stream()
            .map(column -> column.getElement(index))
            .toList();
    }

    public List<List<String>> getRows() {
        List<List<String>> rows = new ArrayList<>();

        for (int i = 0; i < numRows(); i++)
            rows.add(getRow(i));

        return rows;
    }

    public Column getColumn(String header) {
        return columns.get(header);
    }

    public Column getColumn(int index) {
        return columns.getValue(index);
    }

    public void addRow(List<String> row) {
        if (row.size() != numColumns())
            throw new IllegalArgumentException("Row must have same number of elements as the number of columns.");

        for (int i = 0; i < columns.size(); i++) {
            Column column = getColumn(i);

            column.addElement(row.get(i));
        }
    }

    public void addColumn(String header, List<String> elements) {
        Column column = new Column(header, elements);

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
            for (List<String> row : table.getRows())
                result.addRow(row);

        return result;
    }
}
