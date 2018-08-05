package rdthk.httpd;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Response {
    private final OutputStream outputStream;
    private String httpVersion;
    private int statusCode;
    private String reasonPhrase;
    private Map<String, String> headers = new HashMap<>();

    private boolean commited = false;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
        setHttpVersion("HTTP/1.1");
        setStatusCode(200);
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;

        switch (statusCode) {
            case 100: reasonPhrase = "Continue"; break;
            case 101: reasonPhrase = "Switching Protocols"; break;
            case 200: reasonPhrase = "OK"; break;
            case 201: reasonPhrase = "Created"; break;
            case 202: reasonPhrase = "Accepted"; break;
            case 203: reasonPhrase = "Non-Authoritative Information"; break;
            case 204: reasonPhrase = "No Content"; break;
            case 205: reasonPhrase = "Reset Content"; break;
            case 206: reasonPhrase = "Partial Content"; break;
            case 300: reasonPhrase = "Multiple Choices"; break;
            case 301: reasonPhrase = "Moved Permanently"; break;
            case 302: reasonPhrase = "Found"; break;
            case 303: reasonPhrase = "See Other"; break;
            case 304: reasonPhrase = "Not Modified"; break;
            case 305: reasonPhrase = "Use Proxy"; break;
            case 307: reasonPhrase = "Temporary Redirect"; break;
            case 400: reasonPhrase = "Bad Request"; break;
            case 401: reasonPhrase = "Unauthorized"; break;
            case 402: reasonPhrase = "Payment Required"; break;
            case 403: reasonPhrase = "Forbidden"; break;
            case 404: reasonPhrase = "Not Found"; break;
            case 405: reasonPhrase = "Method Not Allowed"; break;
            case 406: reasonPhrase = "Not Acceptable"; break;
            case 407: reasonPhrase = "Proxy Authentication Required"; break;
            case 408: reasonPhrase = "Request Time-out"; break;
            case 409: reasonPhrase = "Conflict"; break;
            case 410: reasonPhrase = "Gone"; break;
            case 411: reasonPhrase = "Length Required"; break;
            case 412: reasonPhrase = "Precondition Failed"; break;
            case 413: reasonPhrase = "Request Entity Too Large"; break;
            case 414: reasonPhrase = "Request-URI Too Large"; break;
            case 415: reasonPhrase = "Unsupported Media Type"; break;
            case 416: reasonPhrase = "Requested range not satisfiable"; break;
            case 417: reasonPhrase = "Expectation Failed"; break;
            case 500: reasonPhrase = "Internal Server Error"; break;
            case 501: reasonPhrase = "Not Implemented"; break;
            case 502: reasonPhrase = "Bad Gateway"; break;
            case 503: reasonPhrase = "Service Unavailable"; break;
            case 504: reasonPhrase = "Gateway Time-out"; break;
            case 505: reasonPhrase = "HTTP Version not supported"; break;
        }
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
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

    public OutputStream getOutputStream() {
        commit();
        return outputStream;
    }

    public boolean isCommited() {
        return commited;
    }

    private void commit() {
        try {
            String encoding = headers.getOrDefault("Content-encoding", "UTF-8");
            byte[] bytes = buildResponseString().getBytes(encoding);
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new HttpdException(e);
        }
    }

    private String buildResponseString() {
        StringBuilder text = new StringBuilder();
        text.append(httpVersion);
        text.append(" ");
        text.append(statusCode);
        text.append(" ");
        text.append(reasonPhrase);
        text.append("\r\n");

        for (Map.Entry<String, String> header: headers.entrySet()) {
            text.append(header.getKey());
            text.append(": ");
            text.append(header.getValue());
            text.append("\r\n");
        }

        return text.toString();
    }
}
