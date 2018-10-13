package toilari.questionapp.data;

import java.sql.SQLException;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Named object that can be referenced from question, so that the reference is
 * one-to-many.
 */
@Data
@AllArgsConstructor
public abstract class NamedQuestionField {
    private final int id;
    private final String name;

    public abstract List<Question> getQuestions(QuestionDao dao) throws SQLException;

    public NamedQuestionField(String name) {
        // Call AllArgsConstructor
        this(-1, name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
