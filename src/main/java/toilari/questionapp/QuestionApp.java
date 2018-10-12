package toilari.questionapp;

import java.util.HashMap;
import java.util.Map;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import toilari.questionapp.data.Answer;
import toilari.questionapp.data.AnswerDao;
import toilari.questionapp.data.Course;
import toilari.questionapp.data.CourseDao;
import toilari.questionapp.data.Question;
import toilari.questionapp.data.QuestionDao;
import toilari.questionapp.data.Topic;
import toilari.questionapp.data.TopicDao;
import toilari.questionapp.database.Database;
import toilari.questionapp.view.NamedQuestionFieldRoutes;
import toilari.questionapp.view.QuestionRoutes;

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

        Spark.get("/questions", QuestionRoutes.get(questions, courses, topics), new ThymeleafTemplateEngine());
        Spark.post("/questions", QuestionRoutes.postAdd(questions, courses, topics));
        Spark.post("/delete/question", QuestionRoutes.postDelete(questions, answers));

        Spark.get("/topics", NamedQuestionFieldRoutes.get("Topic", topics), new ThymeleafTemplateEngine());
        Spark.post("/topics", NamedQuestionFieldRoutes.postAdd("Topic", topics, Topic::new));
        Spark.post("/delete/topic", NamedQuestionFieldRoutes.postDelete("Topic", topics, questions));

        Spark.get("/courses", NamedQuestionFieldRoutes.get("Course", courses), new ThymeleafTemplateEngine());
        Spark.post("/courses", NamedQuestionFieldRoutes.postAdd("Course", courses, Course::new));
        Spark.post("/delete/course", NamedQuestionFieldRoutes.postDelete("Course", courses, questions));

        Spark.get("/answers", (req, res) -> {
            val map = new HashMap<>();
            map.put("questions", questions.findAll());
            map.put("answers", answers.findAll());

            return new ModelAndView(map, "answers");
        }, new ThymeleafTemplateEngine());

        Spark.post("/answers", (req, res) -> {
            try {
                val questionId = Integer.parseInt(req.queryParams("question_id"));
                val text = req.queryParams("text");
                val correct = req.queryParams("correct") != null;

                val question = questions.find(questionId);
                if (question == null) {
                    return "Invalid request: Invalid question ID!";
                }

                answers.add(new Answer(question, text, correct));
            } catch (NumberFormatException ignored) {
            }

            res.redirect("/answers");
            return "";
        });

        Spark.post("/delete/answer", (req, res) -> {
            try {
                val id = Integer.parseInt(req.queryParams("id"));
                answers.remove(id);
            } catch (NumberFormatException ignored) {
            }

            res.redirect("/answers");
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
