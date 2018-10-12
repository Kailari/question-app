package toilari.questionapp.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import toilari.questionapp.database.Database;
import toilari.questionapp.database.SimpleIntPKDao;

/**
 * AnswerDao
 */
@Slf4j
public class AnswerDao extends SimpleIntPKDao<Answer> {
    private static final String[] FIELDS = new String[] { "question_id", "text", "correct" };

    private final QuestionDao questions;

    public AnswerDao(Database db, QuestionDao questions) {
        super(db, "Answer", FIELDS);
        this.questions = questions;
    }

    public void remove(int id) throws SQLException {
        try (val connection = getDb().getConnection();
                val stmt = connection.prepareStatement("DELETE FROM " + getTableName() + " WHERE id=?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void removeAllForQuestion(int id) throws SQLException {
        try (val connection = getDb().getConnection();
                val stmt = connection.prepareStatement("DELETE FROM " + getTableName() + " WHERE question_id=?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Answer> getAnswersForQuestionId(int questionId) throws SQLException {
        try (val connection = getDb().getConnection();
                val stmt = connection.prepareStatement("SELECT * FROM " + getTableName() + " WHERE question_id=?")) {
            stmt.setInt(1, questionId);
            val result = stmt.executeQuery();

            val results = new ArrayList<Answer>();
            while (result.next()) {
                results.add(newData(result));
            }

            return results;
        }
    }

    @Override
    protected Answer newData(ResultSet res) throws SQLException {
        val id = res.getInt("id");
        val text = res.getString("text");
        val correct = res.getBoolean("correct");

        val questionId = res.getInt("question_id");
        val question = questions.find(questionId);
        if (question == null) {
            LOG.error("Could not find valid question in relation to the answer with id=%d!", id);
        }

        return new Answer(id, question, text, correct);
    }

    @Override
    protected void setAddParameters(PreparedStatement stmt, Answer data) throws SQLException {
        stmt.setInt(1, data.getQuestion().getId());
        stmt.setString(2, data.getText());
        stmt.setBoolean(3, data.isCorrect());
    }
}
