package pt.up.fe.els2023.utils;

import pt.up.fe.els2023.config.fields.FileField;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
