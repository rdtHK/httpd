package rdthk.httpd;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestTest {

    @Test
    void testRequestParsing() throws IOException {
        Request request = new Request(stream("GET * HTTP/1.1\r\n" +
                "header-A: foo\r\n" +
                "header-B   :bar \r\n" +
                "\r\n" +
                "body"
        ));

        assertEquals("GET", request.getMethod());
        assertEquals("*", request.getPath());
        assertEquals("HTTP/1.1", request.getHttpVersion());

        assertEquals("foo", request.getHeader("header-A"));
        assertEquals("bar", request.getHeader("header-B"));

        assertEquals("body", read(request.getInputStream()));
    }

    private String read(InputStream stream) {
        try {
            byte[] bytes = stream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    InputStream stream(String text) {
        return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }
}
