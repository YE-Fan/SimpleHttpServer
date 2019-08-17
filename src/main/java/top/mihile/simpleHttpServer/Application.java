package top.mihile.simpleHttpServer;

import java.io.IOException;

public class Application {
    public static void main(String[] args) {
        try {
            SimpleHttpServer.setPort(8080);
            SimpleHttpServer.setBasePath("E:\\JAVA_code\\http_server\\static");
            SimpleHttpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
