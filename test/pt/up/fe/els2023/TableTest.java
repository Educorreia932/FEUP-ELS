package pt.up.fe.els2023;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import pt.up.fe.els2023.load.YamlLoader;
import pt.up.fe.els2023.model.table.Column;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TableTest {
    private Table table;

    @Before
    public void initialize() {
        table = new Table(
            Arrays.asList(
                "First name",
                "Last name",
                "Age"
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

        table.addColumn("Height", Arrays.asList("1.75", "1.60"));

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

        c.addColumn("C", List.of("1"));
        b.addColumn("B", List.of(c));
        a.addColumn("0", List.of("0"));
        a.addColumn("1", List.of(b));

        expected.addColumn("A", List.of(a));
        expected.addColumn("D", List.of("2"));
        
        File file = new File("resources/test.yaml");
        Map<String, Object> contents = new YamlLoader().load(file);
        Table table = Table.fromContents(contents);

        assertEquals(table, expected);
    }
}
