package pt.up.fe.els2023.instructions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;
import pt.up.fe.els2023.model.DataContext;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.utils.FileUtils;

public class SaveInstruction implements Instruction {
    private final DataContext data;
    private final String file;
    private final String tableName;

    public SaveInstruction(DataContext data, String file, String tableName) {
        this.data = data;
        this.file = file;
        this.tableName = tableName;
    }

    @Override
    public void execute() {
        Table table = data.getTable(tableName);
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

            File saveFile = new File("target/" + file);
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
