package toilari.questionapp;

import lombok.extern.slf4j.Slf4j;

/**
 * Application main entry point.
 */
@Slf4j
public class App {
    private static final String PORT_ENV_NAME = "PORT";
    private static final int DEFAULT_PORT = 4567;

    public static void main(String[] args) {
        LOG.info("Starting application");

        final int port = getPort();
        QuestionApp app = new QuestionApp();

        app.run(port);        
    }

    private static int getPort() {
        if (System.getenv(PORT_ENV_NAME) != null) {
            return Integer.parseInt(System.getenv(PORT_ENV_NAME));
        }

        return DEFAULT_PORT;
    }
}
