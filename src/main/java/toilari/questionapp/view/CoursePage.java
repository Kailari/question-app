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
public class CoursePage {
    public static Route postAddQuestion(CourseDao courses, QuestionDao questions, TopicDao topics) {
        return (req, res) -> {
            String redirect = "/courses";
            try {
                val courseId = Integer.parseInt(req.params("id"));
                redirect = "/courses/" + courseId;

                val topicId = Integer.parseInt(req.queryParams("topic_id"));
                val question = req.queryParams("question");
                
                val course = courses.find(courseId);
                if (course == null) {
                    System.err.println("Invalid request: Course ID=" + courseId + " not found!");
                }

                val topic = topics.find(topicId);
                if (topic == null) {
                    System.err.println("Invalid request: Topic ID=" + topicId + " not found!");
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

    public static TemplateViewRoute getSingleCoursePage(CourseDao courses, TopicDao topics, QuestionDao questions) {
        return (req, res) -> {
            val map = new HashMap<String, Object>();
            try {
                val id = Integer.parseInt(req.params("id"));
                map.put("course", courses.find(id));
                map.put("topics", topics.findAll());
                map.put("questions", questions.getQuestionsForCourse(id));
                map.put("addAction", "/courses/" + id + "/question");

            } catch (NumberFormatException e) {
                System.err.println("Error parsing ID: " + e);
            }

            return new ModelAndView(map, "course");
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

                val course = question.getCourse();
                if (course == null) {
                    System.err.printf("Course for question ID=%d not found!\n", questionId);
                }

                redirect = "/courses/" + course.getId();
                questions.remove(questionId, answers);
            } catch (NumberFormatException ignored) {
            }

            res.redirect(redirect);
            return "";
        };
    }
}
