package pt.up.fe.els2023.instructions;

import pt.up.fe.els2023.config.fields.commands.KeysField;

import java.util.List;

public class SelectInstruction implements Instruction {
    private String from = null;
    private List<KeysField> keys = null;

    public SelectInstruction(String from, List<KeysField> keys) {
        this.from = from;
        this.keys = keys;
    }

    public SelectInstruction(String metadata, String rename) {

    }
    
    @Override
    public void execute() {
        
    }
}
