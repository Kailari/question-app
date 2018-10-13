package toilari.questionapp.view;

import java.util.HashMap;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import spark.ModelAndView;
import spark.Route;
import spark.TemplateViewRoute;
import toilari.questionapp.data.AnswerDao;
import toilari.questionapp.data.Course;
import toilari.questionapp.data.CourseDao;
import toilari.questionapp.data.QuestionDao;

/**
 * Static factories for generating routes for course listing page
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoursesPage {
    public static TemplateViewRoute getCourseListPage(CourseDao courses) {
        return (req, res) -> {
            val map = new HashMap<String, Object>();
            map.put("courses", courses.findAll());

            return new ModelAndView(map, "courses");
        };
    }

    public static Route postAdd(CourseDao courses) {
        return (req, res) -> {
            String name = req.queryParams("name");
            courses.add(new Course(name));
            res.redirect("/courses");
            return "";
        };
    }

    public static Route postDeleteCourse(CourseDao courses, QuestionDao questions, AnswerDao answers) {
        return (req, res) -> {
            try {
                val id = Integer.parseInt(req.params("id"));
                courses.remove(id, questions, answers);
            } catch (NumberFormatException ignored) {
            }

            res.redirect("/courses");
            return "";
        };
    }
}