package toilari.questionapp.view;

import java.util.HashMap;
import java.util.function.Function;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import spark.ModelAndView;
import spark.Route;
import spark.TemplateViewRoute;
import toilari.questionapp.data.AnswerDao;
import toilari.questionapp.data.NamedQuestionField;
import toilari.questionapp.data.NamedQuestionFieldDao;
import toilari.questionapp.data.QuestionDao;

/**
 * Factories for generating routes for named question fields (Courses, Topics,
 * etc.)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NamedQuestionFieldRoutes {
    public static <TData extends NamedQuestionField> TemplateViewRoute get(String name,
            NamedQuestionFieldDao<TData> dao) {
        return (req, res) -> {
            val map = new HashMap<String, Object>();
            map.put("objectName", name);
            map.put("objects", dao.findAll());
            map.put("postAction", "/" + name.toLowerCase() + "s");
            map.put("deleteAction", "/delete/" + name.toLowerCase());

            return new ModelAndView(map, "namedquestionfield");
        };
    }

    public static <TData extends NamedQuestionField> Route postAdd(String name, NamedQuestionFieldDao<TData> dao,
            Function<String, TData> factory) {
        return (req, res) -> {
            String dataName = req.queryParams("name");
            dao.add(factory.apply(dataName));

            res.redirect("/" + name.toLowerCase() + "s");
            return "";
        };
    }

    public static <TData extends NamedQuestionField> Route postDelete(String name, NamedQuestionFieldDao<TData> dao,
            QuestionDao questions, AnswerDao answers) {
        return (req, res) -> {
            try {
                int id = Integer.parseInt(req.queryParams("id"));
                dao.remove(id, questions, answers);
            } catch (NumberFormatException ignored) {
            }

            res.redirect("/" + name.toLowerCase() + "s");
            return "";
        };
    }
}
