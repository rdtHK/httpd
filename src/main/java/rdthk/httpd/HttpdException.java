package rdthk.httpd;

public class HttpdException extends RuntimeException {
    public HttpdException(Throwable e) {
        super(e);
    }

    public HttpdException(String message) {
        super(message);
    }
}
