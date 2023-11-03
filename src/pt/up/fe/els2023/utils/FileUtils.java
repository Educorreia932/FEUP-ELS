package pt.up.fe.els2023.utils;

import pt.up.fe.els2023.config.fields.FileField;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public enum FileTypes {
        YAML,
        HTML,
        LATEX,
        CSV
        // TODO: Accept more type of files
    }

    public static FileTypes getFileType(File file) {
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
        return FileTypes.valueOf(fileExtension.toUpperCase());
    }

    public static void setFilesRelativePath(String configRelativePath, List<FileField> files) {
        String relativePath = configRelativePath.substring(0, configRelativePath.lastIndexOf("/"));
        for (FileField entry : files) {
            entry.file = relativePath + "/" + entry.file;
        }
    }

    public static void createDirectory(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}
