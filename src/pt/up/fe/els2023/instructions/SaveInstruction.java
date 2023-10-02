package pt.up.fe.els2023.instructions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVWriter;
import pt.up.fe.els2023.model.DataSingleton;
import pt.up.fe.els2023.model.table.Table;
import pt.up.fe.els2023.utils.FileUtils;

public class SaveInstruction implements Instruction {
    private final DataSingleton data;
    private final String file;
    private final String tableName;

    public SaveInstruction(DataSingleton data, String file, String tableName) {
        this.data = data;
        this.file = file;
        this.tableName = tableName;
    }
    
    @Override
    public void execute() {
        Table table = data.getTable(tableName);
        try {
            FileUtils.createDirectory("target");
            File saveFile = new File("target/" +file);
            FileWriter fileWriter = new FileWriter(saveFile);
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            csvWriter.writeAll(table.getRows());
            csvWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
