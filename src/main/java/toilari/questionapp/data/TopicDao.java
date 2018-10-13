package toilari.questionapp.data;

import java.sql.SQLException;

import lombok.val;
import toilari.questionapp.database.Database;

/**
 * TopicDao
 */
public class TopicDao extends NamedQuestionFieldDao<Topic> {
    public TopicDao(Database db) {
        super(db, "Topic", Topic::new);
    }

    /**
     * Removes the topic and all of its questions.
     * 
     * @param id        ID of the topic to remove
     * @param questions Question DAO to use for removing questions
     * @throws SQLException If removing questions (via
     *                      {@link QuestionDao#removeAllFromTopic(Integer)}), the
     *                      database connection or the DELETE-query fails
     */
    @Override
    public void remove(Integer id, QuestionDao questions, AnswerDao answers) throws SQLException {
        val topic = find(id);
        topic.getQuestions(questions).forEach((q) -> {
            try {
                questions.remove(q.getId(), answers);
            } catch (SQLException ignored) {
            }
        });
        super.remove(id, questions, answers);
	}
}
