package rdthk.httpd;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileLoaderTest {
    @Test
    void testLoader() {
        FileLoader loader = new FileLoader("src/test/resources");
        Path path = loader.load("foo.txt");
        assertTrue(Files.exists(path));
    }

    @Test
    void testFileDoesNotExist() {
        FileLoader loader = new FileLoader("src/test/resources");
        Path path = loader.load("foobar.txt");
        assertNull(path);
    }
}
