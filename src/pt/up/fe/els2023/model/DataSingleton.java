package pt.up.fe.els2023.model;

import pt.up.fe.els2023.model.table.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSingleton {
    // TODO: Change to a table of tables (?)
    private final Map<String, Table> tables = new HashMap<>();
    private final List<FileData> filesData = new ArrayList<>();

    public DataSingleton() {
    }

    public Table getTable(String name) {
        return tables.get(name);
    }

    public void addTable(Table table) {
        tables.put(table.getName(), table);
    }

    public List<FileData> getFilesData() {
        return filesData;
    }

    public void addFilesData(FileData fileData) {
        this.filesData.add(fileData);
    }
}
