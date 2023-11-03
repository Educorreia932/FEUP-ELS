package pt.up.fe.els2023.instructions;

import java.io.BufferedWriter;
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


        File saveFile = new File("target/" + file);
        FileUtils.FileTypes fileType = FileUtils.getFileType(new File(file));
        switch (fileType) {
            case CSV -> saveToCSV(saveFile, headerLines, rowLines);
            case HTML -> saveToHTML(saveFile, headerLines, rowLines);
            case LATEX -> saveToLatex();
        }

    }

    private void saveToCSV(File saveFile, String[] headers, List<String[]> rows) {
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

    private void saveToHTML(File saveFile, String[] headers, List<String[]> rows)  {
        Table table = data.getTable(tableName);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            writer.write("<html>");
            writer.write("<head><title>Save File</title></head>");
            writer.write("<body>");

            writer.write("<table border=\"1\">");

            writer.write("<tr>");
            for (String header : headers) {
                writer.write("<th>" + header + "</th>");
            }
            writer.write("</tr>");
            for (String[] row : rows) {
                writer.write("<tr>");
                for (String cell : row) {
                    writer.write("<td>" + cell + "</td>");
                }
                writer.write("</tr>");
            }

            writer.write("</table>");
            writer.write("</body>");
            writer.write("</html>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } ;

    }

    private void saveToLatex() {

    }
}
