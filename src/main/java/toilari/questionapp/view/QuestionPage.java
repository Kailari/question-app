package toilari.questionapp.view;

import java.util.HashMap;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import spark.ModelAndView;
import spark.Route;
import spark.TemplateViewRoute;
import toilari.questionapp.data.Answer;
import toilari.questionapp.data.AnswerDao;
import toilari.questionapp.data.QuestionDao;

/**
 * QuestionPage
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionPage {
    public static TemplateViewRoute getSingleQuestionPage(QuestionDao questions, AnswerDao answers) {
        return (req, res) -> {
            val map = new HashMap<>();
            try {
                val id = Integer.parseInt(req.params("id"));
                val question = questions.find(id);

                map.put("question", question);
                map.put("answers", question.getAnswers(answers));
                map.put("addAction", "/questions/" + id + "/answer");
            } catch (NumberFormatException ignored) {
            }

            return new ModelAndView(map, "question");
        };
    }

    public static Route postAddAnswer(QuestionDao questions, AnswerDao answers) {
        return (req, res) -> {
            String redirect = "/questions";
            try {
                val questionId = Integer.parseInt(req.params("id"));
                redirect = "/questions/" + questionId;
                val question = questions.find(questionId);
                val text = req.queryParams("text");
                val correct = req.queryParams("correct") != null;

                answers.add(new Answer(question, text, correct));
            } catch (NumberFormatException ignored) {
            }

            res.redirect(redirect);
            return "";
        };
    }

	public static Route postDeleteAnswer(AnswerDao answers, QuestionDao questions) {
		return (req, res) -> {
            String redirect = "/questions";
            try {
                val id = Integer.parseInt(req.params("id"));
                val answer = answers.find(id);
                if (answer == null) {
                    System.err.printf("Could not find answer with ID=%d\n", id);
                }

                answers.remove(id);
                
                val question = answer.getQuestion();
                if (question == null) {
                    System.err.printf("Answer with ID=%d points to an invalid question\n", id);
                } else {   
                    redirect = "/questions/" + question.getId();
                }
            } catch (NumberFormatException ignored) {
            }

            res.redirect(redirect);
            return "";
        };
	}
}
