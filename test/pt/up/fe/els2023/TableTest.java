package pt.up.fe.els2023;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import pt.up.fe.els2023.model.table.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableTest {
    private Table table;

    @Before
    public void initialize() {
        table = new Table(
            "test_table",
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
}
