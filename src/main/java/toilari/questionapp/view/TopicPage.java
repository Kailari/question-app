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
 * Static factories for generating routes for course info page
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicPage {
    public static Route postAddQuestion(CourseDao courses, QuestionDao questions, TopicDao topics) {
        return (req, res) -> {
            String redirect = "/topics";
            try {
                val topicId = Integer.parseInt(req.params("id"));
                redirect = "/topics/" + topicId;

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
            
            res.redirect(redirect);
            return "";
        };
    }

    public static TemplateViewRoute getSingleTopicPage(CourseDao courses, TopicDao topics, QuestionDao questions) {
        return (req, res) -> {
            val map = new HashMap<String, Object>();
            try {
                val id = Integer.parseInt(req.params("id"));
                map.put("topic", topics.find(id));
                map.put("courses", courses.findAll());
                map.put("questions", questions.getQuestionsForTopic(id));
                map.put("addAction", "/topics/" + id + "/question");

            } catch (NumberFormatException e) {
                System.err.println("Error parsing ID: " + e);
            }

            return new ModelAndView(map, "topic");
        };
    }

    public static Route postDeleteQuestion(QuestionDao questions, AnswerDao answers) {
        return (req, res) -> {
            String redirect = "/questions";
            try {
                val questionId = Integer.parseInt(req.params("id"));
                val question = questions.find(questionId);
                if (question == null) {
                    System.err.printf("Question ID=%d not found!\n", questionId);
                    return "";
                }

                val topic = question.getTopic();
                if (topic == null) {
                    System.err.printf("Topic for question ID=%d not found!\n", questionId);
                }

                redirect = "/topics/" + topic.getId();
                questions.remove(questionId, answers);
            } catch (NumberFormatException ignored) {
            }

            res.redirect(redirect);
            return "";
        };
    }
}
