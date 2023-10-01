package pt.up.fe.els2023.instructions;

import pt.up.fe.els2023.load.YamlLoader;
import pt.up.fe.els2023.model.DataSingleton;
import pt.up.fe.els2023.utils.FileUtils;

import java.io.File;
import java.util.Map;

public class LoadInstruction implements Instruction {
    private final DataSingleton data;
    private final File file;

    public LoadInstruction(DataSingleton data, String filePath) {
        this.data = data;
        this.file = new File(filePath);
    }

    @Override
    public void execute() {
        FileUtils.FileTypes fileType = FileUtils.getFileType(file);

        Map<String, Object> fileData = switch (fileType) {
            case YAML -> new YamlLoader().load(file);
            // TODO: Add more cases
        };
        
        data.addFilesData(fileData);
    }
}
