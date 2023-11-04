package pt.up.fe.els2023.save;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HTMLSaver implements Saver{
    @Override
    public void save(File saveFile, String[] headers, List<String[]> rows) {
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
        };
    }
}
