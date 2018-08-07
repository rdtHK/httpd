package rdthk.httpd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Httpd {
    private final RequestHandler requestHandler;
    private boolean running;

    public Httpd(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void start(int port) {
        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
            running = true;
        } catch (IOException e) {
            throw new HttpdException(e);
        }

        while (running) {
            try {
                Socket client = server.accept();
                ConnectionHandler connectionHandler = new ConnectionHandler(requestHandler, client);
                connectionHandler.start();
            } catch (IOException e) {
                throw new HttpdException(e);
            }
        }
    }

    public void stop() {
        running = false;
    }

    private class ConnectionHandler extends Thread {
        private final Socket socket;
        private final RequestHandler requestHandler;

        public ConnectionHandler(RequestHandler requestHandler, Socket socket) {
            this.requestHandler = requestHandler;
            this.socket = socket;
        }

        public void run() {
            try {
                Request request = new Request(socket.getInputStream());
                Response response = new Response(socket.getOutputStream());

                requestHandler.handleRequest(request, response);
            } catch (IOException e) {
                throw new HttpdException(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new HttpdException(e);
                }
            }
        }
    }
}
