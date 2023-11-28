package pt.up.fe.els2023.model.table;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.util.Pair;
import org.apache.commons.collections4.map.ListOrderedMap;
import pt.up.fe.els2023.internal.Selection;
import pt.up.fe.els2023.load.JSONLoader;
import pt.up.fe.els2023.load.Loader;
import pt.up.fe.els2023.load.XMLLoader;
import pt.up.fe.els2023.load.YamlLoader;
import pt.up.fe.els2023.model.table.column.Column;
import pt.up.fe.els2023.save.CSVSaver;
import pt.up.fe.els2023.save.HTMLSaver;
import pt.up.fe.els2023.save.LatexSaver;
import pt.up.fe.els2023.save.Saver;
import pt.up.fe.els2023.utils.FileUtils;
import pt.up.fe.els2023.utils.GlobFileVisitor;

import static pt.up.fe.els2023.utils.FileUtils.getFileType;

public class Table {
    private final ListOrderedMap<String, Column> columns = new ListOrderedMap<>();

    public Table() {

    }

    public Table(Column... columns) {
        for (Column column : columns)
            this.columns.put(column.getHeader(), column);
    }

    public static Table withHeaders(List<Pair<String, ValueType>> headers) {
        List<Column> columns = new ArrayList<>();

        for (var header : headers) {
            Column column = Column.withType(header.getKey(), header.getValue());

            columns.add(column);
        }

        return new Table(columns.toArray(Column[]::new));
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
                    Column column;

                    if (row instanceof Map<?, ?>) {
                        column = Column.ofTables(String.valueOf(i));
                        column.addElement(fromContents((Map<String, Object>) row));
                    }

                    else {
                        column = Column.ofStrings(String.valueOf(i));
                        column.addElement(String.valueOf(row));
                    }

                    arrayTable.addColumn(column);

                    i++;
                }

                table.addColumn(Column.ofTables(key, arrayTable));
            }

            // Object
            else if (value instanceof Map<?, ?>)
                table.addColumn(Column.ofTables(key, fromContents((Map<String, Object>) value)));

                // Terminal value
            else {
                if (value == null)
                    value = "null";

                // Convert integer to double
                if (value.getClass() == Integer.class)
                    value = Double.valueOf((Integer) value);

                ValueType type = ValueType.fromObject(value);
                assert type != null;
                table.addColumn(Column.withType(key, type, value));
            }
        }

        return table;
    }

    public static Table load(String pattern) {
        var fileVisitor = new GlobFileVisitor("test/resources/" + pattern);

        try {
            Files.walkFileTree(Paths.get("test/resources/"), fileVisitor);
        }

        catch (Exception e) {
            System.err.println(e.getMessage());
        }

        List<Path> paths = fileVisitor.getMatchedFiles();
        List<Table> tables = new ArrayList<>();

        for (Path path : paths) {
            Table table = fromFile(path.toFile());

            tables.add(table);
        }

        return concat(tables);
    }

    public static Table fromFile(File file) {
        FileUtils.FileTypes fileType = getFileType(file);

        Loader loader = switch (fileType) {
            case YAML -> new YamlLoader();
            case XML -> new XMLLoader();
            case JSON -> new JSONLoader();
            case HTML, TEX, CSV -> throw new RuntimeException("Filetype not supported");
        };

        Map<String, Object> contents;
        
        try {
            contents = loader.load(file);
        }
        
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        
        Table table = Table.fromContents(contents);

        // Metadata fields 
        table.addColumn(Column.ofStrings(String.valueOf(Metadata.FOLDER), file.getParentFile().toString()));
        table.addColumn(Column.ofStrings(String.valueOf(Metadata.FILENAME), file.getName()));

        return table;
    }

    public Table slice(int fromIndex, int toIndex) {
        Table table = Table.withHeaders(getHeadersAndTypes());
        var rows = getRows().subList(fromIndex, toIndex);

        for (var row : rows)
            table.addRow(row);

        return table;
    }

    public Selection select() {
        return new Selection(this);
    }

    public Table selectColumnsByName(String... fieldNames) {
        var table = new Table();

        for (String fieldName : fieldNames) {
            Column selectedColumn = getColumn(fieldName);

            if (Arrays.asList(fieldNames).contains(fieldName)) {
                // Composite value
                if (selectedColumn.getElement(0) instanceof Table) {
                    for (var subTable : selectedColumn.getElements()) {
                        for (var subColumn : ((Table) subTable).getColumns()) {
                            boolean hasColumn = table.getHeaders().contains(subColumn.getHeader());

                            if (!hasColumn)
                                table.addColumn(subColumn);

                            else
                                table.getColumn(subColumn.getHeader()).addElements(subColumn.getElements().toArray());
                        }
                    }
                }

                // Terminal value
                else
                    table.addColumn(selectedColumn);
            }
        }

        return table;
    }

    public Table selectColumnsByType(ValueType valueType) {
        Stream<Column> columns = getColumns().stream()
            .filter(column -> column.getType() != valueType);

        List<String> metadataFields = Arrays.stream(Metadata.values()).map(Objects::toString).toList();
        columns = columns.filter(column -> !metadataFields.contains(column.getHeader()));

        return new Table(columns.toArray(Column[]::new));
    }

    public Table rename(String field, String newName) {
        int index = columns.indexOf(field);
        Column column = columns.remove(field);

        column.setHeader(newName);
        columns.put(index, newName, column);

        return this;
    }

    public Table unflatten() {
        List<Table> tables = new ArrayList<>();

        for (Column column : getColumns()) {
            Table subTable = (Table) column.getElements().get(0);

            tables.add(subTable);
        }

        return concat(tables.toArray(Table[]::new));
    }

    public Table min(String field) {
        List<Object> elements = getColumn(field).getElements();

        if (!(elements.get(0) instanceof Number))
            throw new RuntimeException("Column is not of numberic type.");

        List<Double> numberList = elements.stream().map(e -> (Double) e).toList();
        int index = numberList.indexOf(Collections.min(numberList));

        return slice(index, index + 1);
    }

    public Table max(String field) {
        List<Object> elements = getColumn(field).getElements();

        if (!(elements.get(0) instanceof Number))
            throw new RuntimeException("Column is not of numberic type.");

        List<Double> numberList = elements.stream().map(e -> (Double) e).toList();
        int index = numberList.indexOf(Collections.max(numberList));

        return slice(index, index + 1);
    }

    public static Table merge(Table... tables) {
        List<Column> columns = new ArrayList<>();

        for (Table table : tables)
            columns.addAll(table.getColumns());

        return new Table(columns.toArray(Column[]::new));
    }

    public static Table concat(List<Table> tables) {
        var headers = tables.get(0).getHeadersAndTypes();

        Table concatenatedTable = Table.withHeaders(headers);

        for (Table table : tables) {
            List<Object> row = new ArrayList<>();

            for (Pair<String, ValueType> header : headers) {
                Column column = table.getColumn(header.getKey());

                if (column == null)
                    row.add(null);

                else
                    row.add(column.getElements().get(0));
            }

            concatenatedTable.addRow(row);
        }

        return concatenatedTable;
    }

    public static Table concat(Table... tables) {
        return concat(Arrays.asList(tables));
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

        FileUtils.createDirectory("target");

        File saveFile = new File("target/" + path);
        FileUtils.FileTypes fileType = FileUtils.getFileType(new File(path));

        Saver saver = switch (fileType) {
            case CSV -> new CSVSaver();
            case HTML -> new HTMLSaver();
            case TEX -> new LatexSaver();
            case YAML, JSON, XML -> throw new RuntimeException("Filetype not supported");
        };

        saver.save(saveFile, headerLines, rowLines);
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

    public Column getColumn(String header) {
        if (header.contains(".")) {
            String[] parts = header.split("\\.");
            ListOrderedMap<String, Column> auxColumns = columns;

            for (int i = 0; i < parts.length - 1; i++) {
                Column currentColumn = auxColumns.get(parts[i]);

                for (Object object : currentColumn.getElements())
                    if (object instanceof Table)
                        auxColumns = ((Table) object).columns;
            }

            return auxColumns.get(parts[parts.length - 1]);
        }

        Column column = columns.get(header);

        if (column == null)
            throw new RuntimeException("Table does not have field: " + header);

        return column;
    }

    public Column getColumn(int index) {
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

    public void addColumn(Column column) {
        columns.put(column.getHeader(), column);
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

        columns.forEach((key, column) -> headers.add(column.getHeader()));

        return headers;
    }

    public List<Pair<String, ValueType>> getHeadersAndTypes() {
        List<Pair<String, ValueType>> headers = new ArrayList<>();

        columns.forEach((key, column) -> headers.add(column.getHeaderAndType()));

        return headers;
    }

    public List<Column> getColumns() {
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
        for (var header : getHeaders()) {
            Column thisColumn = getColumn(header);
            Column otherColumn = table.getColumn(header);

            if (!thisColumn.getElements().equals(otherColumn.getElements()))
                return false;
        }

        return true;
    }
}
