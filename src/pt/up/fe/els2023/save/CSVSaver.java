package pt.up.fe.els2023.save;

import com.opencsv.CSVWriter;
import pt.up.fe.els2023.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVSaver implements Saver{
    @Override
    public void save(File saveFile, String[] headers, List<String[]> rows) {
        List<String[]> allLines = new ArrayList<>();
        allLines.add(headers);
        allLines.addAll(rows);

        try {
            FileUtils.createDirectory("target");

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
