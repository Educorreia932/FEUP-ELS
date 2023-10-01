package pt.up.fe.els2023.config;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import pt.up.fe.els2023.config.fields.*;
import pt.up.fe.els2023.config.fields.commands.*;
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

        Constructor constructor = new Constructor(Config.class);

        constructor.addTypeDescription(
            new TypeDescription(SelectField.class, new Tag("!select")));

        constructor.addTypeDescription(
            new TypeDescription(MergeField.class, new Tag("!merge")));

        constructor.addTypeDescription(
            new TypeDescription(FromSelectionField.class, new Tag("!fromSelection")));

        constructor.addTypeDescription(
            new TypeDescription(MetadataSelectionField.class, new Tag("!metadataSelection")));

        Yaml yaml = new Yaml(constructor);
        Config config = yaml.load(inputStream);

        List<FileField> source = config.source;
        List<CommandField> commands = config.commands;
        List<FileField> target = config.target;

        instructions.addAll(parseSource(source));

        for (CommandField command : commands) {
            if (command instanceof SelectField)
                instructions.addAll(parseSelect(((SelectField) command).select));

            else if (command instanceof MergeField)
                instructions.addAll(parseMerge(((MergeField) command).merge));
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
            SelectInstruction instruction = null;

            if (entry instanceof FromSelectionField) {
                instruction = new SelectInstruction(
                    ((FromSelectionField) entry).from,
                    ((FromSelectionField) entry).keys
                );
            }

            else if (entry instanceof MetadataSelectionField) {
                instruction = new SelectInstruction(
                    ((MetadataSelectionField) entry).metadata,
                    ((MetadataSelectionField) entry).rename
                );
            }

            instructions.add(instruction);
        }

        return instructions;
    }

    private List<MergeInstruction> parseMerge(List<MergeEntryField> mergeEntries) {
        List<MergeInstruction> instructions = new ArrayList<>();

        for (MergeEntryField entry : mergeEntries) {
            MergeInstruction instruction = new MergeInstruction(
                entry.sources,
                entry.target
            );

            instructions.add(instruction);
        }

        return instructions;
    }

    private List<SaveInstruction> parseTarget(List<FileField> files) {
        List<SaveInstruction> instructions = new ArrayList<>();

        for (FileField entry : files) {
            SaveInstruction instruction = new SaveInstruction(entry.file, entry.table);

            instructions.add(instruction);
        }

        return instructions;
    }
}
