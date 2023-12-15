package pt.up.fe.els2023.utils;

import java.io.File;

public class FileUtils {
    public enum FileTypes {
        YAML,
        HTML,
        TEX,
        CSV,
        XML,
        JSON
    }

    public static FileTypes getFileType(File file) {
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
        return FileTypes.valueOf(fileExtension.toUpperCase());
    }

    public static void createDirectory(String directoryName) {
        File directory = new File(directoryName);
        
        if (!directory.exists())
            directory.mkdir();
    }
}
