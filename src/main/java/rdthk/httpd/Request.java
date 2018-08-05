package rdthk.httpd;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private String method;
    private String path;
    private String httpVersion;
    private Map<String, String> headers = new HashMap<>();
    private InputStream inputStream;

    public Request() {
    }

    public Request(InputStream stream) throws IOException {
        inputStream = stream;

        readRequestLine();

        String line;

        while (!(line = readLine()).isEmpty()) {
            readHeader(line);
        }

    }

    private void readHeader(String line) {
        int separatorIndex = line.indexOf(":");
        String name = line.substring(0, separatorIndex).trim();
        String value = line.substring(separatorIndex + 1).trim();

        putHeader(name, value);
    }

    private void readRequestLine() throws IOException {
        String line = readLine();
        String[] tokens = line.split(" ");

        if (tokens.length < 3) {
            throw new SyntaxException("Incomplete request line: \"" + line + "\".");
        }

        setMethod(tokens[0].trim());
        setPath(tokens[1].trim());
        setHttpVersion(tokens[2].trim());
    }

    private String readLine() throws IOException {
        StringBuilder line = new StringBuilder();

        int ch;

        while ((ch = inputStream.read()) != '\r') {
            if (ch == -1) {
                throw new SyntaxException("Unexpected end of stream.");
            }

            line.append((char) ch);
        }

        if (inputStream.read() != '\n') {
            throw new SyntaxException("Missing Linefeed character.");
        }

        return line.toString();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void putHeader(String name, String value) {
        headers.put(name, value);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
