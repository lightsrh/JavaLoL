package app;

import app.utils.ServerJava;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("League of Legends backend is booting...");

        int port = System.getenv("SERVER_PORT") != null ? Integer.parseInt(System.getenv("SERVER_PORT")) : 8080;

        ServerJava server = new ServerJava();

        server.javalinApp().start(port);
    }
}
