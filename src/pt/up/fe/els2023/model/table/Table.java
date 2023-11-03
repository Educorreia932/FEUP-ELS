package pt.up.fe.els2023.model.table;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyException;
import java.util.*;
import java.util.stream.Collectors;

import com.opencsv.CSVWriter;
import org.apache.commons.collections4.map.ListOrderedMap;
import pt.up.fe.els2023.load.JSONLoader;
import pt.up.fe.els2023.load.Loader;
import pt.up.fe.els2023.load.XMLLoader;
import pt.up.fe.els2023.load.YamlLoader;
import pt.up.fe.els2023.model.table.values.StringValue;
import pt.up.fe.els2023.model.table.values.TableValue;
import pt.up.fe.els2023.model.table.values.Value;
import pt.up.fe.els2023.utils.FileUtils;

import static pt.up.fe.els2023.utils.FileUtils.getFileType;

public class Table {
    private final ListOrderedMap<String, Column<?>> columns = new ListOrderedMap<>();

    public Table() {

    }

    public Table(Column<?>... columns) {
        for (Column<?> column : columns)
            this.columns.put(column.getHeader(), column);
    }

    public Table(List<String> headers) {
        for (String header : headers) {
            Column<?> column = new Column<>(header);

            columns.put(column.getHeader(), column);
        }
    }

    @SuppressWarnings("unchecked")
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
                        Column<TableValue> column = new Column<>(String.valueOf(i));
                        column.addElement(fromContents((Map<String, Object>) row));

                        arrayTable.addColumn(column);
                    }

                    else {
                        Column<StringValue> column = new Column<>(String.valueOf(i));
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

                // Terminal value
            else {
                if (value == null)
                    value = "null";

                table.addColumn(key, Collections.singletonList(value));
            }
        }

        return table;
    }

    public static Table fromFile(File file) {
        FileUtils.FileTypes fileType = getFileType(file);

        Loader loader = switch (fileType) {
            case YAML -> new YamlLoader();
            case XML -> new XMLLoader();
            case JSON -> new JSONLoader();
        };

        Map<String, Object> contents = loader.load(file);
        Table table = Table.fromContents(contents);

        // Metadata fields 
        table.addColumn(String.valueOf(Metadata.FOLDER), Collections.singletonList(file.getParentFile().toString()));

        return table;
    }

    public static Table concat(List<Table> tables) {
        List<String> headers = tables.get(0).getHeaders();
        Table result = new Table(headers);

        for (Table table : tables)
            for (List<?> row : table.getRows())
                result.addRow(row);

        return result;
    }
    
    public void save(String path) {
        List<String> headers = getHeaders();
        List<List<Object>> rows = getRows();

        String[] headerLines = headers.toArray(String[]::new);
        List<String[]> rowLines = new ArrayList<>();

        for (List<?> row : rows) {
            String[] stringList = row.stream()
                .map(Object::toString)
                .toArray(String[]::new);

            rowLines.add(stringList);
        }

        List<String[]> allLines = new ArrayList<>();

        allLines.add(headerLines);
        allLines.addAll(rowLines);

        try {
            FileUtils.createDirectory("target");

            File saveFile = new File("target/" + path);
            FileWriter fileWriter = new FileWriter(saveFile);
            CSVWriter csvWriter = new CSVWriter(fileWriter);

            csvWriter.writeAll(allLines);
            csvWriter.close();
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Table selectByName(String... fieldNames) {
        var table = new Table();

        for (String fieldName : fieldNames) {
            Column<?> column = getColumn(fieldName);

            if (Arrays.asList(fieldNames).contains(fieldName)) {
                // Composite value
                if (column.getElement(0) instanceof Table subTable) {
                    for (var subColumn : subTable.getColumns())
                        table.addColumn(subColumn);
                }

                // Terminal value
                else
                    table.addColumn(column);
            }
        }

        return table;
    }

    public ArrayList<Object> getRow(int index) {
        return columns.values().stream()
            .map(column -> column.getElement(index))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<List<Object>> getRows() {
        List<List<Object>> rows = new ArrayList<>();

        for (int i = 0; i < numRows(); i++)
            rows.add(getRow(i));

        return rows;
    }

    public Column<?> getColumn(String header) {
        if (header.contains(".")) {
            String[] parts = header.split("\\.");
            ListOrderedMap<String, Column<?>> auxColumns = columns;

            for (int i = 0; i < parts.length - 1; i++) {
                Column<?> currentColumn = auxColumns.get(parts[i]);

                for (Object object : currentColumn.getElements())
                    if (object instanceof Table)
                        auxColumns = ((Table) object).columns;
            }

            return auxColumns.get(parts[parts.length - 1]);
        }

        Column<?> column = columns.get(header);

        if (column == null)
            throw new RuntimeException("Table does not have field: " + header);

        return column;
    }

    public Column<?> getColumn(int index) {
        return columns.getValue(index);
    }

    public void addRow(List<?> row) {
        if (row.size() != numColumns())
            throw new IllegalArgumentException("Row must have same number of elements as the number of columns.");

        for (int i = 0; i < columns.size(); i++) {
            Column<?> column = getColumn(i);

            column.addElement(row.get(i));
        }
    }

    public void addColumn(Column<?> column) {
        columns.put(column.getHeader(), column);
    }

    public void addColumn(String header, List<Object> elements) {
        Column<?> column = new Column<>(header, elements);

        columns.put(header, column);
    }

    public void removeRow(int index) {
        columns.forEach((key, column) -> column.removeElement(index));
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

        columns.forEach((key, value) -> headers.add(value.getHeader()));

        return headers;
    }

    public List<Column<?>> getColumns() {
        return columns.valueList();
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
