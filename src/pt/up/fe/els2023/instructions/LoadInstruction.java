package pt.up.fe.els2023.instructions;

public class LoadInstruction implements Instruction {
    private String filePath;
    
    public LoadInstruction(String filePath) {
        this.filePath = filePath;
    }
    
    @Override
    public void execute() {
    }
}
