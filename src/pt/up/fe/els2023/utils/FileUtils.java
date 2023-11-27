package pt.up.fe.els2023.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public static String getFilePathWithRootSource(File file) {
        String rootSource = "src";
        Path filePathWithRouteSource = Paths.get(rootSource, file.getPath());
        return filePathWithRouteSource.toAbsolutePath().toString();
    }

    public static void createDirectory(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}
