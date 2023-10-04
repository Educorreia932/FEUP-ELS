package pt.up.fe.els2023.instructions;

import pt.up.fe.els2023.config.fields.commands.KeysField;
import pt.up.fe.els2023.model.DataSingleton;
import pt.up.fe.els2023.model.FileData;
import pt.up.fe.els2023.model.table.Table;

import java.util.*;

enum SelectionType {
    FROM,
    METADATA
}

public class SelectInstruction implements Instruction {
    private final DataSingleton data;

    // From selection
    private String from = null;
    private List<KeysField> keysFields = null;

    // Metadata selection
    private String metadata = null;
    private String rename = null;

    private final SelectionType selectionType;

    // TODO: Split into two separate instructions (?)
    public SelectInstruction(DataSingleton data, String from, List<KeysField> keysFields) {
        this.data = data;
        this.from = from;
        this.keysFields = keysFields;
        this.selectionType = SelectionType.FROM;
    }

    public SelectInstruction(DataSingleton data, String metadata, String rename) {
        this.data = data;
        this.metadata = metadata;
        this.rename = rename;
        this.selectionType = SelectionType.METADATA;
    }

    @Override
    public void execute() {
        List<Table> tables = data.getTables();
        List<FileData> filesData = data.getFilesData();

        switch (this.selectionType) {
            case FROM:
                for (int i = 0; i < tables.size(); i++) {
                    Table table = tables.get(i);
                    FileData fileData = filesData.get(i);
                    Map<String, Object> values = fileData.contents();

                    for (String fieldName : from.split("\\."))
                        values = (Map<String, Object>) values.get(fieldName);

                    for (KeysField keysField : keysFields)
                        table.addColumn(keysField.rename, Collections.singletonList(values.get(keysField.name).toString()));
                }

                break;

            case METADATA:
                for (int i = 0; i < tables.size(); i++) {
                    Table table = tables.get(i);
                    FileData fileData = filesData.get(i);

                    table.addColumn(rename, new ArrayList<>(List.of(fileData.name())));
                }

                break;
        }
    }
}
