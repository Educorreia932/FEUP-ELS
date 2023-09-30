package pt.up.fe.els2023.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import pt.up.fe.els2023.instructions.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ConfigParser {
    public List<Instruction> parse(String filename) {
        List<Instruction> instructions = new ArrayList<>();

        InputStream inputStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream(filename);

        Yaml yaml = new Yaml(new Constructor(Config.class));
        Config config = yaml.load(inputStream);

//        List<File> source = config.getSource();
//        List<Command> commands = config.getCommands();
//        List<FileTable> target = config.getTarget();
//
//        instructions.addAll(parseSource(source));
//
//        for (Command command : commands) {
//            if (command instanceof Select)
//                instructions.addAll(parseCommand((Select) command));
//
//            else if (command instanceof Merge)
//                instructions.addAll(parseCommand((Merge) command));
//        }
//
//        instructions.addAll(parseTarget(target));

        return instructions;
    }

    private List<LoadInstruction> parseSource(List<FileEntry> files) {
        List<LoadInstruction> instructions = new ArrayList<>();

        for (FileEntry file : files) {
            String filePath = file.getFile();
            LoadInstruction instruction = new LoadInstruction(filePath);

            instructions.add(instruction);
        }

        return instructions;
    }

    private List<SelectInstruction> parseCommand(Select select) {
        List<SelectInstruction> instructions = new ArrayList<>();
        List<SelectEntry> entries = select.getEntries();

        for (SelectEntry entry : entries) {
            SelectInstruction instruction = new SelectInstruction();

            instructions.add(instruction);
        }

        return instructions;
    }

    private List<MergeInstruction> parseCommand(Merge merge) {
        List<MergeInstruction> instructions = new ArrayList<>();
        List<MergeEntry> entries = merge.getEntries();

        for (MergeEntry entry : entries) {
            MergeInstruction instruction = new MergeInstruction(
                entry.getSources(),
                entry.getTarget()
            );

            instructions.add(instruction);
        }

        return instructions;
    }

    private List<SaveInstruction> parseTarget(List<FileTable> target) {
        List<SaveInstruction> instructions = new ArrayList<>();

        return instructions;
    }
}
