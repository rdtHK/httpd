package rdthk.httpd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class RequestHandlerTest {

    ByteArrayOutputStream stream;
    Response response;
    FileLoader loader = new FileLoader("src/test/resources");
    RequestHandler handler = new RequestHandler(loader);

    @BeforeEach
    void beforeEach() {
        stream = new ByteArrayOutputStream();
        response = new Response(stream);
    }

    @Test
    void test404() throws IOException {
        Request request = makeRequest("GET / HTTP/1.1\r\n\r\n");
        handler.handleRequest(request, response);
        assertTrue(stream.toString().contains("404 Not Found"));
    }

    @Test
    void test405() throws IOException {
        Request request = makeRequest("POST / HTTP/1.1\r\n\r\n");
        handler.handleRequest(request, response);
        assertTrue(stream.toString().contains("405 Method Not Allowed"));
    }

    @Test
    void test200() throws IOException {
        Request request = makeRequest("GET /foo.txt HTTP/1.1\r\n\r\n");
        handler.handleRequest(request, response);
        assertTrue(stream.toString().contains("200 OK"));
    }

    @Test
    void testResponseBody() throws IOException {
        Request request = makeRequest("GET /foo.txt HTTP/1.1\r\n\r\n");
        handler.handleRequest(request, response);
        assertTrue(stream.toString().contains("foobar"));
    }

    @Test
    void testContentType() throws IOException {
        Request request = makeRequest("GET /foo.txt HTTP/1.1\r\n\r\n");
        handler.handleRequest(request, response);
        assertEquals("text/plain", response.getHeader("Content-Type"));
    }

    @Test
    void testContentLength() throws IOException {
        Request request = makeRequest("GET /foo.txt HTTP/1.1\r\n\r\n");
        handler.handleRequest(request, response);
        assertEquals("6", response.getHeader("Content-Length"));
    }

    private Request makeRequest(String text) throws IOException {
        return new Request(new ByteArrayInputStream(text.getBytes()));
    }
}
