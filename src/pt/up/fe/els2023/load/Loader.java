package pt.up.fe.els2023.load;

import java.io.File;
import java.util.Map;

public interface Loader {
    public Map<String, Object> load(File file);
}
