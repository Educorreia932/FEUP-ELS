package pt.up.fe.els2023;

import pt.up.fe.els2023.config.ConfigParser;
import pt.up.fe.els2023.instructions.Instruction;
import pt.up.fe.els2023.model.table.Table;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ConfigParser parser = new ConfigParser();

        List<Instruction> instructions = parser.parse("resources/" + args[0]);
        for (Instruction instruction : instructions) {
            instruction.execute();
        }
    }
}