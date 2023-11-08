package pt.up.fe.els2023;

import pt.up.fe.els2023.config.ConfigParser;
import pt.up.fe.els2023.instructions.Instruction;
import pt.up.fe.els2023.model.table.Table;

import java.nio.file.Paths;
import java.util.List;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {
        ConfigParser parser = new ConfigParser();

        List<Instruction> instructions = parser.parse("resources/checkpoint1/" + args[0]);
        for (Instruction instruction : instructions) {
            instruction.execute();
        }
    }
}