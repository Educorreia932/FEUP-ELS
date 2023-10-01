package pt.up.fe.els2023.instructions;

import pt.up.fe.els2023.load.YamlLoader;
import pt.up.fe.els2023.utils.FileUtils;

import java.io.File;

public class LoadInstruction implements Instruction {
    private final File file;

    public LoadInstruction(String filePath) {
        this.file = new File(filePath);
    }
    
    @Override
    public void execute() {
        FileUtils.FileTypes fileType = FileUtils.getFileType(file);

        switch (fileType) {
            case YAML -> new YamlLoader().load(file);
            // TODO: Add more cases
        }
    }
}
