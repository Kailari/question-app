package toilari.questionapp.view;

import java.util.HashMap;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import spark.ModelAndView;
import spark.Route;
import spark.TemplateViewRoute;
import toilari.questionapp.data.CourseDao;
import toilari.questionapp.data.Question;
import toilari.questionapp.data.QuestionDao;
import toilari.questionapp.data.TopicDao;

/**
 * Static factories for generating routes for question list page
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionsPage {
    public static Route postAddQuestion(QuestionDao questions, CourseDao courses, TopicDao topics) {
        return (req, res) -> {
            try {
                val topicId = Integer.parseInt(req.queryParams("topic_id"));
                val courseId = Integer.parseInt(req.queryParams("course_id"));
                val question = req.queryParams("question");

                val topic = topics.find(topicId);
                if (topic == null) {
                    System.err.println("Invalid request: Topic ID=" + topicId + " not found!");
                }

                val course = courses.find(courseId);
                if (course == null) {
                    System.err.println("Invalid request: Course ID=" + courseId + " not found!");
                }

                if (question == null || question.isEmpty()) {
                    System.err.println("Invalid request: Question cannot be empty!");
                }

                questions.add(new Question(course, topic, question));

            } catch (NumberFormatException e) {
                System.err.println("Failed to parse int: " + e);
            }
            
            res.redirect("/questions");
            return "";
        };
    }

    public static TemplateViewRoute getFullQuestionList(QuestionDao questions, CourseDao courses, TopicDao topics) {
        return (req, res) -> {
            val map = new HashMap<String, Object>();
            try {
                map.put("courses", courses.findAll());
                map.put("topics", topics.findAll());
                map.put("questions", questions.findAll());

            } catch (NumberFormatException e) {
                System.err.println("Error parsing ID: " + e);
            }

            return new ModelAndView(map, "questions");
        };
    }
}
