package pt.up.fe.els2023;

import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import pt.up.fe.els2023.load.YamlLoader;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.model.table.ValueType;
import pt.up.fe.els2023.model.table.column.Column;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TableTest {
    private Table table;

    @Before
    public void initialize() {
        table = Table.withHeaders(
            List.of(
                new Pair<>("First name", ValueType.STRING),
                new Pair<>("Last name", ValueType.STRING),
                new Pair<>("Age", ValueType.DOUBLE)
            )
        );
    }

    @Test
    public void addRow() {
        table.addRow(Arrays.asList("John", "Doe", "22"));
        table.addRow(Arrays.asList("Jane", "Doe", "23"));

        assertEquals(2, table.numRows());
        assertEquals(3, table.numColumns());
    }

    @Test
    public void addColumn() {
        table.addRow(Arrays.asList("John", "Doe", "22"));
        table.addRow(Arrays.asList("Jane", "Doe", "23"));

        table.addColumn(Column.ofDoubles("Height", 1.75, 1.60));

        assertEquals(2, table.numRows());
        assertEquals(4, table.numColumns());
    }

    @Test
    public void removeColumn() {
        table.addRow(Arrays.asList("John", "Doe", "22"));
        table.addRow(Arrays.asList("Jane", "Doe", "23"));

        table.removeColumn("Age");

        assertEquals(2, table.numRows());
        assertEquals(2, table.numColumns());
    }

    @Test
    public void removeRow() {
        table.addRow(Arrays.asList("John", "Doe", "22"));
        table.addRow(Arrays.asList("Jane", "Doe", "23"));

        table.removeRow(0);

        assertEquals(1, table.numRows());
        assertEquals(3, table.numColumns());
    }

    @Test
    public void fromContents() {
        Table expected = new Table();

        Table a = new Table();
        Table b = new Table();
        Table c = new Table();

        c.addColumn(Column.ofDoubles("C", 1.0));
        b.addColumn(Column.ofTables("B", c));
        a.addColumn(Column.ofDoubles("0", 0.0));
        a.addColumn(Column.ofTables("1", b));

        expected.addColumn(Column.ofTables("A", a));
        expected.addColumn(Column.ofDoubles("D", 2.0));

        File file = new File("resources/test.yaml");
        Map<String, Object> contents = new YamlLoader().load(file);
        Table table = Table.fromContents(contents);

        assertEquals(table, expected);
    }
}
