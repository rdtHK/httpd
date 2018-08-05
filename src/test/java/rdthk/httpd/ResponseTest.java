package rdthk.httpd;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

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
        assertEquals("HTTP/1.1 200 OK\r\n\r\n", out.toString());
    }
}
