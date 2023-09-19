package pt.up.fe.els2023;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class Parser {
    void parse(String filename) {
        Yaml yaml = new Yaml();

        InputStream inputStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream(filename);
        
        Map<String, Object> obj = yaml.load(inputStream);
        
        System.out.println(obj);
    }
}
