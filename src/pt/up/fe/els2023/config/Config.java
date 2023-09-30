package pt.up.fe.els2023.config;

import java.util.List;

interface Command {
}

class Select implements Command {
    private List<SelectEntry> entries;

    public List<SelectEntry> getEntries() {
        return entries;
    }
}

interface SelectEntry {
}

class FromSelection implements SelectEntry {
    private String from;
    private List<Field> fields;
}

class Field {
    private String name;
    private String rename;
}

class MetadataSelection implements SelectEntry {
    private String metadata;
    private String rename;
}

class Merge implements Command {
    private List<MergeEntry> entries;

    public List<MergeEntry> getEntries() {
        return entries;
    }
}

class MergeEntry {
    private List<String> sources;
    private String target;

    public List<String> getSources() {
        return sources;
    }

    public String getTarget() {
        return target;
    }
}

class FileTable extends FileEntry {
    private String table;
}

public class Config {
    private List<FileEntry> source;
/*    private List<Command> commands;
    private List<FileTable> target;*/

    public List<FileEntry> getSource() {
        return source;
    }

/*    public List<Command> getCommands() {
        return commands;
    }

    public List<FileTable> getTarget() {
        return target;
    }*/
}
