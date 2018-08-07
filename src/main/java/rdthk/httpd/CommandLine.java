package rdthk.httpd;

public class CommandLine {

    public static void main(String[] args) {
        String port = "3000";
        String path = System.getProperty("user.dir");

        if (args.length >= 1) {
            port = args[0];
        }

        Httpd httpd = new Httpd(new RequestHandler(new FileLoader(path)));
        System.out.printf("Http server started.\n");
        System.out.printf("Port: %s.\n", port);
        System.out.printf("Root: %s.\n", path);
        httpd.start(Integer.parseInt(port));
    }

}
