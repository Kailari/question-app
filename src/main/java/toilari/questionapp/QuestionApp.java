package toilari.questionapp;

import lombok.extern.slf4j.Slf4j;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import toilari.questionapp.data.AnswerDao;
import toilari.questionapp.data.CourseDao;
import toilari.questionapp.data.QuestionDao;
import toilari.questionapp.data.TopicDao;
import toilari.questionapp.database.Database;
import toilari.questionapp.view.CoursePage;
import toilari.questionapp.view.CoursesPage;
import toilari.questionapp.view.QuestionPage;
import toilari.questionapp.view.QuestionsPage;
import toilari.questionapp.view.TopicPage;
import toilari.questionapp.view.TopicsPage;

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

        Spark.get("/questions", QuestionsPage.getFullQuestionList(questions, courses, topics), new ThymeleafTemplateEngine());
        Spark.post("/questions", QuestionsPage.postAddQuestion(questions, courses, topics));

        Spark.get("/topics", TopicsPage.getTopicListPage(topics), new ThymeleafTemplateEngine());
        Spark.post("/topics", TopicsPage.postAdd(topics));
        Spark.post("/delete/topic/:id", TopicsPage.postDeleteTopic(topics, questions));

        Spark.get("/topics/:id", TopicPage.getSingleTopicPage(courses, topics, questions), new ThymeleafTemplateEngine());
        Spark.post("/topics/:id/question", TopicPage.postAddQuestion(courses, questions, topics));


        Spark.get("/courses", CoursesPage.getCourseListPage(courses), new ThymeleafTemplateEngine());
        Spark.post("/courses", CoursesPage.postAdd(courses));
        Spark.post("/delete/course/:id", CoursesPage.postDeleteCourse(courses, questions));
        
        Spark.get("/courses/:id", CoursePage.getSingleCoursePage(courses, topics, questions), new ThymeleafTemplateEngine());
        Spark.post("/courses/:id/question", CoursePage.postAddQuestion(courses, questions, topics));
        Spark.post("/delete/question/:id", CoursePage.postDeleteQuestion(questions, answers));
        
        Spark.get("/questions/:id", QuestionPage.getSingleQuestionPage(questions, answers), new ThymeleafTemplateEngine());
        Spark.post("/questions/:id/answer", QuestionPage.postAddAnswer(questions, answers));
        Spark.post("/delete/answer/:id", QuestionPage.postDeleteAnswer(answers, questions));

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
