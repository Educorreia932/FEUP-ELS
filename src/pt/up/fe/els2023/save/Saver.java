package pt.up.fe.els2023.save;

import java.io.File;
import java.util.List;

public interface Saver {
    void save(File saveFile, String[] headers, List<String[]> rows);
}
