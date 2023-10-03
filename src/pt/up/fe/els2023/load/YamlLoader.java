package pt.up.fe.els2023.load;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public class YamlLoader implements Loader {
    @Override
    public Map<String, Object> load(File file) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file.getPath());
        Yaml yaml = new Yaml();
        return yaml.load(inputStream);
    }
}
