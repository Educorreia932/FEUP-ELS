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
        data.getTable(tableName).save(file);
    }
}
