package pt.up.fe.els2023.instructions;

public class SaveInstruction implements Instruction {
    private final String file;
    private final String table;

    public SaveInstruction(String file, String table) {
        this.file = file;
        this.table = table;
    }
    
    @Override
    public void execute() {
        
    }
}
