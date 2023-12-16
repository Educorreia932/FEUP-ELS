package pt.up.fe.els2023.load;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import pt.up.fe.els2023.utils.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class JSONLoader implements Loader {
    @Override
    public Map<String, Object> load(File file) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();

            return gson.fromJson(reader, type);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
