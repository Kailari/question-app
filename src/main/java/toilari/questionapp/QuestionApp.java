package toilari.questionapp;

import lombok.var;
import lombok.extern.slf4j.Slf4j;
import spark.Route;
import spark.Spark;

import java.util.Map;
import java.util.HashMap;

/**
 * Main application
 */
@Slf4j
public class QuestionApp {
    private final Map<String, Route> postUrls;
    private final Map<String, Route> getUrls;

    public QuestionApp() {
        postUrls = new HashMap<>();
        getUrls = new HashMap<>();

        getUrls.put("*", (res, req) -> {
            return "Hello World!";
        });
    }

    public void run(int port) {
        Spark.port(port);

        for (var entry : postUrls.entrySet()) {
            Spark.post(entry.getKey(), entry.getValue());
        }

        for (var entry : getUrls.entrySet()) {
            Spark.get(entry.getKey(), entry.getValue());
        }

        LOG.info("Application running");
    }
}
