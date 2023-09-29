package pt.up.fe.els2023.config;

import java.util.List;

class File {
    private String file;
}

interface Command {
    
}

class Select implements Command {
    private List<SelectEntry> entries;
}

class SelectEntry {
    private List<FromSelection> fromSelection;
    private MetadataSelection metadataSelection;
}

class FromSelection {
    private String from;
    private List<Field> fields;
}

class Field {
    private String name;
    private String rename;
}

class MetadataSelection {
    private String metadata;
    private String rename;
}

class Merge implements Command {
    private List<MergeEntry> entries;
}

class MergeEntry {
    private List<String> sources;
    private String target;
}

class FileTable extends File {
    private String table;
}

class Source {
    private List<File> files;
}

class Target {
    private List<FileTable> files;
}

public class Config {
    private Source source;
    private List<Command> commands;
    private Target target;

    public Source getSource() {
        return source;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Target getTarget() {
        return target;
    }
}
