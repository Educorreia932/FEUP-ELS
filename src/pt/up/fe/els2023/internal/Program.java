package pt.up.fe.els2023.internal;

import com.opencsv.CSVWriter;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Program {
    private final List<Table> tables;

    private Program() {
        tables = new ArrayList<>();
    }
    
    public static Program program() {
        return new Program();
    }
    
    void addTable(Table table) {
        tables.add(table);
    }

    public TableBuilder load(String path) {
        File file = new File(path);
        Table loadedTable = Table.fromFile(file);
        
        return new TableBuilder(this, loadedTable);
    }
    
    public void save(String path) {
        Table table = tables.get(0);
        List<String> headers = table.getHeaders();
        List<List<Object>> rows = table.getRows();

        String[] headerLines = headers.toArray(String[]::new);
        List<String[]> rowLines = new ArrayList<>();

        for (List<?> row : rows) {
            String[] stringList = row.stream()
                .map(Object::toString)
                .toArray(String[]::new);

            rowLines.add(stringList);
        }

        List<String[]> allLines = new ArrayList<>();

        allLines.add(headerLines);
        allLines.addAll(rowLines);

        try {
            FileUtils.createDirectory("target");

            File saveFile = new File("target/" + path);
            FileWriter fileWriter = new FileWriter(saveFile);
            CSVWriter csvWriter = new CSVWriter(fileWriter);

            csvWriter.writeAll(allLines);
            csvWriter.close();
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
