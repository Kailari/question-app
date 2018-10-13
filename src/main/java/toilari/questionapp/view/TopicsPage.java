package toilari.questionapp.view;

import java.util.HashMap;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import spark.ModelAndView;
import spark.Route;
import spark.TemplateViewRoute;
import toilari.questionapp.data.AnswerDao;
import toilari.questionapp.data.QuestionDao;
import toilari.questionapp.data.Topic;
import toilari.questionapp.data.TopicDao;

/**
 * Static factories for generating routes for course listing page
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicsPage {
    public static TemplateViewRoute getTopicListPage(TopicDao topics) {
        return (req, res) -> {
            val map = new HashMap<String, Object>();
            map.put("topics", topics.findAll());

            return new ModelAndView(map, "topics");
        };
    }

    public static Route postAdd(TopicDao topics) {
        return (req, res) -> {
            String name = req.queryParams("name");
            topics.add(new Topic(name));
            res.redirect("/topics");
            return "";
        };
    }

    public static Route postDeleteTopic(TopicDao topics, QuestionDao questions, AnswerDao answers) {
        return (req, res) -> {
            try {
                val id = Integer.parseInt(req.params("id"));
                topics.remove(id, questions, answers);
            } catch (NumberFormatException ignored) {
            }

            res.redirect("/topics");
            return "";
        };
    }
}