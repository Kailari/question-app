package toilari.questionapp.data;

import java.sql.SQLException;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Answer
 */
@Data
@AllArgsConstructor
public class Answer {
    private final int id;
    private Question question;
    private String text;
    private boolean correct;

    public Answer(QuestionDao questions, int questionId, String text, boolean correct) throws SQLException {
        this(questions.find(questionId), text, correct);
    }

    public Answer(Question question, String text, boolean correct) {
        // Call AllArgsConstructor
        this(-1, question, text, correct);
    }
}
