package pt.up.fe.els2023.config;

import org.yaml.snakeyaml.Yaml;
import pt.up.fe.els2023.instructions.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ConfigParser {
    public List<Instruction> parse(String filename) {
        List<Instruction> instructions = new ArrayList<>();

        Yaml yaml = new Yaml();

        InputStream inputStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream(filename);

        Config config = yaml.load(inputStream);

        Source source = config.getSource();
        List<Command> commands = config.getCommands();
        Target target = config.getTarget();

        return instructions;
    }

    private LoadInstruction parseSource(Source source) {
        return new LoadInstruction();
    }

    private SelectInstruction parseCommand(Select select) {
        return new SelectInstruction();
    }

    private MergeInstruction parseCommand(Merge merge) {
        return new MergeInstruction();
    }

    private SaveInstruction parseTarget(Target target) {
        return new SaveInstruction();
    }
}
