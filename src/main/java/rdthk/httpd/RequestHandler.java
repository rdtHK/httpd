package rdthk.httpd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RequestHandler {
    private final FileLoader loader;

    public RequestHandler(FileLoader loader) {
        this.loader = loader;
    }

    public void handleRequest(Request request, Response response) {
        if (!request.getMethod().equals("GET")) {
            response.setStatusCode(405);
            response.end();
            return;
        }

        Path path = loader.load(request.getPath());

        if (path == null) {
            response.setStatusCode(404);
            response.end();
            return;
        }

        try {
            response.setStatusCode(200);
            response.putHeader("Content-Length", String.valueOf(Files.size(path)));
            response.putHeader("Content-Type", Files.probeContentType(path));
            response.write(Files.newInputStream(path));
        } catch (IOException e) {
            response.setStatusCode(500);
            response.end();
        }
    }
}
