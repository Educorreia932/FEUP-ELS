package pt.up.fe.els2023.model;

import pt.up.fe.els2023.model.table.Table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.map.ListOrderedMap;

public class DataContext {
    // TODO: Change to a table of tables (?)
    private final ListOrderedMap<String, Table> tables = new ListOrderedMap<>();
    private final List<FileData> filesData = new ArrayList<>();

    public DataContext() {
    }

    public List<Table> getTables() {
        return tables.valueList();
    }

    public Table getTable(String name) {
        return tables.get(name);
    }

    public void addTable(String name, Table table) {
        tables.put(name, table);
    }

    public List<FileData> getFilesData() {
        return filesData;
    }

    public void addFilesData(FileData fileData) {
        this.filesData.add(fileData);
    }
}
