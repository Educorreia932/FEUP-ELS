package pt.up.fe.els2023.save;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LatexSaver implements Saver{
    @Override
    public void save(File saveFile, String[] headers, List<String[]> rows) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            writer.write("\\documentclass{article}\n");
            writer.write("\\usepackage{array}\n");
            writer.write("\\begin{document}\n");
            writer.write("\\begin{center}\n");

            // Create table
            writer.write("\\begin{tabular}{");
            for(int i = 0; i < headers.length; i++) {
                writer.write("|c");
            }
            writer.write("|}\n");
            writer.write("\\hline\n");

            // Table headers
            for(int i = 0; i < headers.length-1; i++) {
                String currentHeader = headers[i].replaceAll("_", "\\\\_");
                writer.write(currentHeader + " & ");
            }
            String lastHeader = headers[headers.length-1].replaceAll("_", "\\\\_");
            writer.write(lastHeader + "\\\\\n");

            writer.write("\\hline\n");

            // Table rows
            for(String[] row : rows) {
                for(int i = 0; i < row.length-1; i++) {
                    String currentRow = row[i].replaceAll("_", "\\\\_");
                    writer.write(currentRow + " & ");
                }
                String lastRow = row[row.length-1].replaceAll("_", "\\\\_");
                writer.write(lastRow + "\\\\\n");
            }

            writer.write("\\hline\n");
            writer.write("\\end{tabular}\n");
            writer.write("\\end{center}\n");
            writer.write("\\end{document}\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
