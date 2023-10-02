package pt.up.fe.els2023.model;

import java.util.Map;

public class FileData {
    private Map<String, Object> content;
    private String name;

    public FileData(Map<String, Object> content, String name) {
        this.content = content;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getContent() {
        return content;
    }
}
