package pt.up.fe.els2023.model;

import pt.up.fe.els2023.model.table.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSingleton {
    // TODO: Change to a table of tables (?)
    private final Map<String, Table> tables = new HashMap<>();
    private final List<Map<String, Object>> filesData = new ArrayList<>();

    public DataSingleton() {
    }

    public Table getTable(String name) {
        return tables.get(name);
    }

    public void addTable(String name, Table table) {
        tables.put(name, table);
    }

    public List<Map<String, Object>> getFilesData() {
        return filesData;
    }

    public void addFilesData(Map<String, Object> fileData) {
        this.filesData.add(fileData);
    }
}
