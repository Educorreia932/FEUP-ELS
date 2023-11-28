package pt.up.fe.els2023.utils;

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class GlobFileVisitor extends SimpleFileVisitor<Path> {
    private final PathMatcher pathMatcher;
    private final List<Path> matchedFiles = new ArrayList<>();

    public GlobFileVisitor(final String glob) {
        this.pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) {
        if (pathMatcher.matches(path))
            matchedFiles.add(path);

        return FileVisitResult.CONTINUE;
    }

    public List<Path> getMatchedFiles() {
        return matchedFiles;
    }
}
