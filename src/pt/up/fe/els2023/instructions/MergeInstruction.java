package pt.up.fe.els2023.instructions;

import java.util.List;

public class MergeInstruction implements Instruction {
    private final List<String> sources;
    private final String target;

    public MergeInstruction(List<String> sources, String target) {
        this.sources = sources;
        this.target = target;
    }
    
    @Override
    public void execute() {
        
    }
}
