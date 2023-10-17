package pt.up.fe.els2023.instructions;

import pt.up.fe.els2023.config.fields.commands.KeysField;
import pt.up.fe.els2023.model.DataContext;
import pt.up.fe.els2023.model.table.Column;
import pt.up.fe.els2023.model.table.Table;

import java.util.*;

enum SelectionType {
    FROM,
    METADATA
}

public class SelectInstruction implements Instruction {
    private final DataContext dataContext;

    // From selection
    private String from = null;
    private List<KeysField> keysFields = null;

    // Metadata selection
    private String metadata = null;
    private String rename = null;

    private final SelectionType selectionType;

    // TODO: Split into two separate instructions (?)
    public SelectInstruction(DataContext dataContext, String from, List<KeysField> keysFields) {
        this.dataContext = dataContext;
        this.from = from;
        this.keysFields = keysFields;
        this.selectionType = SelectionType.FROM;
    }

    public SelectInstruction(DataContext data, String metadata, String rename) {
        this.dataContext = data;
        this.metadata = metadata;
        this.rename = rename;
        this.selectionType = SelectionType.METADATA;
    }

    @Override
    public void execute() {
        List<List> entries = dataContext.getEntries();

        switch (this.selectionType) {
            case FROM:

                // Iterate over tables for every file
                for (List entry : entries) {
                    Table selectionTable = new Table();
                    Table fileTable = (Table) entry.get(2);
                    Table selection = (Table) fileTable.getColumn(from).getElements().get(0);

                    for (KeysField keysField : keysFields) {
                        Column<?> column = selection.getColumn(keysField.name);
                        column.setHeader(keysField.rename);

                        selectionTable.addColumn(column);
                    }

                    // Replace table
                    dataContext.replaceTable((String) entry.get(1), selectionTable);

                    // TODO: Nested sub-fields
                }

                break;

            case METADATA:
                for (List<?> entry : entries) {
                    Table fileTable = (Table) entry.get(2);
                    String filename = (String) entry.get(0);

                    fileTable.addColumn(rename, List.of(filename));
                }

                break;
        }
    }
}
