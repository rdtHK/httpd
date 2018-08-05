package rdthk.httpd;

import java.io.IOException;
import java.io.InputStream;

public class RequestReader {
    public Request read(InputStream stream) throws IOException {
        Request request = new Request();

        readRequestLine(stream, request);

        String line;

        while (!(line = readLine(stream)).isEmpty()) {
            readHeader(request, line);
        }

        request.setInputStream(stream);

        return request;
    }

    private void readHeader(Request request, String line) {
        int separatorIndex = line.indexOf(":");
        String name = line.substring(0, separatorIndex).trim();
        String value = line.substring(separatorIndex + 1).trim();

        request.putHeader(name, value);
    }

    private void readRequestLine(InputStream stream, Request request) throws IOException {
        String line = readLine(stream);
        String[] tokens = line.split(" ");

        if (tokens.length < 3) {
            throw new SyntaxException("Incomplete request line: \"" + line + "\".");
        }

        request.setMethod(tokens[0].trim());
        request.setPath(tokens[1].trim());
        request.setHttpVersion(tokens[2].trim());
    }

    private String readLine(InputStream stream) throws IOException {
        StringBuilder line = new StringBuilder();

        int ch;

        while ((ch = stream.read()) != '\r') {
            if (ch == -1) {
                throw new SyntaxException("Unexpected end of stream.");
            }

            line.append((char) ch);
        }

        if (stream.read() != '\n') {
            throw new SyntaxException("Missing Linefeed character.");
        }

        return line.toString();
    }
}
