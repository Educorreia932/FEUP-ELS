package pt.up.fe.els2023.internal;

import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.column.Column;
import pt.up.fe.els2023.save.CSVSaver;
import pt.up.fe.els2023.save.HTMLSaver;
import pt.up.fe.els2023.save.LatexSaver;
import pt.up.fe.els2023.save.Saver;
import pt.up.fe.els2023.utils.FileUtils;
import pt.up.fe.els2023.utils.GlobFileVisitor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class TableInteraction {
    private Table table;

    private TableInteraction(Table table) {
        this.table = table;
    }

    public static TableInteraction fromTable(Table table) {
        return new TableInteraction(table);
    }

    public static TableInteraction load(String pattern) {
        var fileVisitor = new GlobFileVisitor("test/resources/" + pattern);

        try {
            Files.walkFileTree(Paths.get("test/resources/"), fileVisitor);
        }

        catch (Exception e) {
            System.err.println(e.getMessage());
        }

        List<Path> paths = fileVisitor.getMatchedFiles();
        List<TableInteraction> tables = new ArrayList<>();

        for (Path path : paths) {
            Table table = Table.fromFile(path.toFile());

            tables.add(fromTable(table));
        }

        return new TableInteraction(concat(tables.toArray(TableInteraction[]::new)).getTable());
    }

    public static TableInteraction merge(TableInteraction... interactions) {
        List<Table> tables = Arrays.stream(interactions).map(TableInteraction::getTable).toList();

        return new TableInteraction(Table.merge(tables));
    }

    public static TableInteraction concat(TableInteraction... interactions) {
        List<Table> tables = Arrays.stream(interactions).map(TableInteraction::getTable).toList();

        return fromTable(Table.concat(tables));
    }

    public Selection select() {
        return new Selection(this);
    }

    public TableInteraction rename(String field, String newName) {
        table = table.rename(field, newName);

        return this;
    }

    public TableInteraction unstack() {
        table = table.unstack();

        return this;
    }

    public TableInteraction stack() {
        table = table.stack();

        return this;
    }

    public TableInteraction max(String fieldName) {
        table = table.max(fieldName);

        return this;
    }

    public void save(String path) {
        table = table.unravel();

        List<String> headers = table.getHeaders();
        List<List<Object>> rows = table.getRows();

        String[] headerLines = headers.toArray(String[]::new);
        List<String[]> rowLines = new ArrayList<>();

        for (List<?> row : rows) {
            String[] stringList = row.stream()
                .map(s -> s == null ? "" : s)
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

    public TableInteraction slice(int i, int j) {
        table = table.slice(i, j);

        return this;
    }

    public TableInteraction forEach(Function<TableInteraction, TableInteraction> action) {
        // Array table
        for (Column column : table.getColumns()) {
            Table subTable = (Table) column.getElement(0);
            TableInteraction interaction = fromTable(subTable);
            Table result = action.apply(interaction).getTable();

            column.setElement(0, result);
        }

        return this;
    }

    public TableInteraction unravel() {
        table = table.unravel();

        return this;
    }

    public Table getTable() {
        return table;
    }

    private List<Object> sum() {
        List<Object> row = new ArrayList<>();

        for (Column column : table.getColumns())
            row.add(column.sum());

        return row;
    }

    private List<Object> average() {
        List<Object> row = new ArrayList<>();

        for (Column column : table.getColumns())
            row.add(column.average());

        return row;
    }

    public TableInteraction aggregate(Aggregation... aggregations) {
        List<List<Object>> rows = new ArrayList<>();

        for (Aggregation aggregation : aggregations) {
            rows.add(
                switch (aggregation) {
                    case SUM -> sum();
                    case AVERAGE -> average();
                }
            );
        }
        
        for (List<Object> row : rows)
            table.addRow(row);

        return this;
    }
}
