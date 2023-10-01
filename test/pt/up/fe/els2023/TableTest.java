package pt.up.fe.els2023;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import javafx.util.Pair;

import pt.up.fe.els2023.model.table.Table;
import static pt.up.fe.els2023.model.table.TabularDataType.*;

public class TableTest {
    private Table table;

    @Before
    public void initialize() {
        table = new Table(
            new Pair<>("First name", STRING),
            new Pair<>("Last name", STRING),
            new Pair<>("Age", INTEGER)
        );
    }    
    
    @Test
    public void addRow() {
        table.addRow("John", "Doe", 22);
        table.addRow("Jane", "Doe", 23);

        assertEquals(2, table.numRows());
        assertEquals(3, table.numColumns());
    }
    
    @Test
    public void addColumn() {
        table.addRow("John", "Doe", 22);
        table.addRow("Jane", "Doe", 23);
        
        table.addColumn("Height", 1.75, 1.60);

        assertEquals(2, table.numRows());
        assertEquals(4, table.numColumns());
    }

    @Test
    public void removeColumn() {
        table.addRow("John", "Doe", 22);
        table.addRow("Jane", "Doe", 23);

        table.removeColumn("Age");

        assertEquals(2, table.numRows());
        assertEquals(2, table.numColumns());
    }

    @Test
    public void removeRow() {
        table.addRow("John", "Doe", 22);
        table.addRow("Jane", "Doe", 23);

        table.removeRow(0);

        assertEquals(1, table.numRows());
        assertEquals(3, table.numColumns());
    }
}
