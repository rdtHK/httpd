package rdthk.httpd;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ResponseTest {
    @Test
    void testResponseLine() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Response response = new Response(out);
        response.setStatusCode(404);
        response.getOutputStream(); // commits the response and returns the output stream.
        assertTrue(out.toString().startsWith("HTTP/1.1 404 Not Found\r\n"));
    }

    @Test
    void testHeaders() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Response response = new Response(out);
        response.putHeader("Content-Type", "text/plain");
        response.putHeader("X-Header-2", "foo");
        response.getOutputStream(); // commits the response and returns the output stream.
        assertTrue(out.toString().contains("Content-Type: text/plain\r\n"));
        assertTrue(out.toString().contains("X-Header-2: foo\r\n"));
    }

    @Test
    void testDefaultResponse() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Response response = new Response(out);
        response.getOutputStream(); // commits the response and returns the output stream.
        assertTrue(response.isCommitted());
        assertEquals("HTTP/1.1 200 OK\r\n\r\n", out.toString());
    }

    @Test
    void testInputStreamWrite() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("foobar".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Response response = new Response(out);
        response.write(in);
        assertTrue(response.isCommitted());
        assertEquals("HTTP/1.1 200 OK\r\n\r\nfoobar", out.toString());
    }
}
