package pt.up.fe.els2023.config;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import pt.up.fe.els2023.config.fields.*;
import pt.up.fe.els2023.config.fields.commands.CommandField;
import pt.up.fe.els2023.config.fields.commands.SelectEntryField;
import pt.up.fe.els2023.config.fields.commands.SelectField;
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

        LoaderOptions loaderOptions = new LoaderOptions();
        Yaml yaml = new Yaml(new Constructor(Config.class, loaderOptions));
        Config config = yaml.load(inputStream);

        List<FileField> source = config.source;
        List<SelectField> commands = config.commands;
        List<FileField> target = config.target;

        instructions.addAll(parseSource(source));

        for (CommandField command : commands) {
            if (command instanceof SelectField)
                instructions.addAll(parseSelect(((SelectField) command).select));

//            else if (command instanceof Merge)
//                instructions.addAll(parseCommand((Merge) command));
        }

        instructions.addAll(parseTarget(target));

        return instructions;
    }

    private List<LoadInstruction> parseSource(List<FileField> files) {
        List<LoadInstruction> instructions = new ArrayList<>();

        for (FileField entry : files) {
            String filePath = entry.file;
            LoadInstruction instruction = new LoadInstruction(filePath);

            instructions.add(instruction);
        }

        return instructions;
    }

    private List<SelectInstruction> parseSelect(List<SelectEntryField> selectEntries) {
        List<SelectInstruction> instructions = new ArrayList<>();
        
        for (SelectEntryField entry : selectEntries) {
            SelectInstruction instruction = new SelectInstruction(entry.from, entry.keys);
            
            instructions.add(instruction);
        }

        return instructions;
    }

//    private List<MergeInstruction> parseCommand(Merge merge) {
//        List<MergeInstruction> instructions = new ArrayList<>();
//        List<MergeEntry> entries = merge.getEntries();
//
//        for (MergeEntry entry : entries) {
//            MergeInstruction instruction = new MergeInstruction(
//                entry.getSources(),
//                entry.getTarget()
//            );
//
//            instructions.add(instruction);
//        }
//
//        return instructions;
//    }

    private List<SaveInstruction> parseTarget(List<FileField> files) {
        List<SaveInstruction> instructions = new ArrayList<>();

        for (FileField entry : files) {
            SaveInstruction instruction = new SaveInstruction(entry.file, entry.table);

            instructions.add(instruction);
        }

        return instructions;
    }
}
