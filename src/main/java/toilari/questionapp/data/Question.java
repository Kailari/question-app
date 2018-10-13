package toilari.questionapp.data;

import java.sql.SQLException;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Question
 */
@Data
@AllArgsConstructor
public class Question {
    private final int id;
    private final Course course;
    private final Topic topic;
    private final String question;

    public List<Answer> getAnswers(AnswerDao dao) throws SQLException {
        return dao.getAnswersForQuestionId(this.id);
    }

    public String getInfoUrl() {
        return "/questions/" + id;
    }

    public String getDeleteUrl() {
        return "/delete/question/" + id;
    }

    public Question(Course course, Topic topic, String question) {
        // Call AllArgsConstructor
        this(-1, course, topic, question);
    }
}
