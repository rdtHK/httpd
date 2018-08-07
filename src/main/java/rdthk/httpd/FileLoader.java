package rdthk.httpd;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileLoader {
    private final String basePath;

    public FileLoader(String basePath) {
        this.basePath = basePath;
    }

    public Path load(String path) {
        Path file = Paths.get(basePath, path);

        if (Files.exists(file) && !Files.isDirectory(file)) {
            return file;
        } else {
            return null;
        }
    }
}
