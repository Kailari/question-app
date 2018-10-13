package toilari.questionapp.view;

import java.util.HashMap;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import spark.ModelAndView;
import spark.Route;
import spark.TemplateViewRoute;
import toilari.questionapp.data.AnswerDao;
import toilari.questionapp.data.CourseDao;
import toilari.questionapp.data.Question;
import toilari.questionapp.data.QuestionDao;
import toilari.questionapp.data.TopicDao;

/**
 * Factories for constructing routes for questions-page.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionRoutes {
    public static TemplateViewRoute get(QuestionDao questions, CourseDao courses, TopicDao topics) {
        return (req, res) -> {
            val map = new HashMap<String, Object>();
            map.put("questions", questions.findAll());
            map.put("courses", courses.findAll());
            map.put("topics", topics.findAll());

            return new ModelAndView(map, "questions");
        };
    }

    public static Route postAdd(QuestionDao questions, CourseDao courses, TopicDao topics) {
        return (req, res) -> {
            System.out.println("Question add request: " + req.toString());
            try {
                val courseId = Integer.parseInt(req.queryParams("course_id"));
                val topicId = Integer.parseInt(req.queryParams("topic_id"));
                val question = req.queryParams("question");

                val course = courses.find(courseId);
                if (course == null) {
                    return "Invalid request: Course ID=" + courseId + " not found!";
                }

                val topic = topics.find(topicId);
                if (topic == null) {
                    return "Invalid request: Topic ID=" + topicId + " not found!";
                }

                if (question == null || question.isEmpty()) {
                    return "Invalid request: Question cannot be empty!";
                }

                questions.add(new Question(course, topic, question));
            } catch (NumberFormatException ignored) {
            }

            String redirect = req.queryParams("redirect");
            res.redirect(redirect == null ? "/questions" : redirect);
            return "";
        };
    }

    public static Route postDelete(QuestionDao questions, AnswerDao answers) {
        return (req, res) -> {
            try {
                val id = Integer.parseInt(req.params("id"));
                questions.remove(id, answers);
            } catch (NumberFormatException ignored) {
            }

            String redirect = req.queryParams("redirect");
            res.redirect(redirect == null ? "/questions" : redirect);
            return "";
        };
    }


	public static TemplateViewRoute getOne(QuestionDao questions, CourseDao courses, TopicDao topics, AnswerDao answers) {
        return (req, res) -> {
            val map = new HashMap<String, Object>();
            

            return new ModelAndView(map, "");
        };
	}
}
