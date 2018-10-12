package toilari.questionapp;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import toilari.questionapp.data.AnswerDao;
import toilari.questionapp.data.Course;
import toilari.questionapp.data.CourseDao;
import toilari.questionapp.data.QuestionDao;
import toilari.questionapp.data.Topic;
import toilari.questionapp.data.TopicDao;
import toilari.questionapp.database.Database;

/**
 * Main application
 */
@Slf4j
public class QuestionApp {
    public void run(int port) {
        Spark.port(port);

        Database db = new Database(getDatabaseAddress());

        TopicDao topics = new TopicDao(db);
        CourseDao courses = new CourseDao(db);
        QuestionDao questions = new QuestionDao(db, courses, topics);
        AnswerDao answers = new AnswerDao(db, questions);

        Spark.get("/questions", (res, req) -> {
            Map map = new HashMap<>();

            return new ModelAndView(map, "questions");
        }, new ThymeleafTemplateEngine());


        Spark.get("/topics", (res, req) -> {
            Map map = new HashMap<>();
            map.put("objectName", "Topic");
            map.put("objects", topics.findAll());
            map.put("postAction", "/topics");
            map.put("deleteAction", "/delete/topic");

            return new ModelAndView(map, "topicscourses");
        }, new ThymeleafTemplateEngine());

        Spark.post("/topics", (res, req) -> {
            String name = res.queryParams("name");
            topics.add(new Topic(name));

            req.redirect("/topics");
            return "";
        });

        Spark.post("/delete/topic", (res, req) -> {
            try {
                int id = Integer.parseInt(res.queryParams("id"));
                topics.remove(id, questions);
            } catch (NumberFormatException ignored) {
            }

            req.redirect("/topics");
            return "";
        });


        Spark.get("/courses", (res, req) -> {
            Map map = new HashMap<>();
            map.put("objectName", "Course");
            map.put("objects", courses.findAll());
            map.put("postAction", "/courses");
            map.put("deleteAction", "/delete/course");

            return new ModelAndView(map, "topicscourses");
        }, new ThymeleafTemplateEngine());

        Spark.post("/courses", (res, req) -> {
            String name = res.queryParams("name");
            courses.add(new Course(name));

            req.redirect("/courses");
            return "";
        });

        Spark.post("/delete/course", (res, req) -> {
            try {
                int id = Integer.parseInt(res.queryParams("id"));
                courses.remove(id, questions);
            } catch (NumberFormatException ignored) {
            }

            req.redirect("/courses");
            return "";
        });


        Spark.get("*", (res, req) -> {
            // TODO: Index page
            req.redirect("/questions");
            return "";
        });

        LOG.info("Application running");
    }

    private String getDatabaseAddress() {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return dbUrl;
        }

        return "jdbc:sqlite:questions.db";
    }
}
