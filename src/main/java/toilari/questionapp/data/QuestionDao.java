package toilari.questionapp.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.val;
import toilari.questionapp.database.Database;
import toilari.questionapp.database.SimpleIntPKDao;

/**
 * QuestionDao
 */
public class QuestionDao extends SimpleIntPKDao<Question> {
    private static final String[] FIELDS = new String[] { "course_id", "topic_id", "question" };

    private final CourseDao courses;
    private final TopicDao topics;

    public QuestionDao(Database db, CourseDao courses, TopicDao topics) {
        super(db, "Question", FIELDS);
        this.courses = courses;
        this.topics = topics;
    }

    /**
     * Removes a question and all of its answers
     * 
     * @param id      ID of the question to remove
     * @param answers {@link AnswerDao} to use for removing associated answers
     */
    public void remove(int id, AnswerDao answers) throws SQLException {
        answers.removeAllForQuestion(id);

        try (val connection = getDb().getConnection();
                val stmt = connection.prepareStatement("DELETE FROM " + getTableName() + " WHERE id=?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Gets all questions for a topic
     * 
     * @param topicId ID of the topic which questions to get
     * @throws SQLException If the connection to the database or the SELECT-query
     *                      fails.
     */
    public List<Question> getQuestionsForTopic(Integer topicId) throws SQLException {
        try (val connection = getDb().getConnection();
                val stmt = connection.prepareStatement("SELECT * FROM " + getTableName() + " WHERE topic_id=?")) {
            stmt.setInt(1, topicId);
            val res = stmt.executeQuery();
            val results = new ArrayList<Question>();

            while (res.next()) {
                results.add(newData(res));
            }

            return results;
        }
    }

    /**
     * Gets all questions for a course
     * 
     * @param courseId ID of the course which questions to get
     * @throws SQLException If the connection to the database or the SELECT-query
     *                      fails.
     */
    public List<Question> getQuestionsForCourse(Integer courseId) throws SQLException {
        try (val connection = getDb().getConnection();
                val stmt = connection.prepareStatement("SELECT * FROM " + getTableName() + " WHERE course_id=?")) {
            stmt.setInt(1, courseId);
            val res = stmt.executeQuery();
            val results = new ArrayList<Question>();

            while (res.next()) {
                results.add(newData(res));
            }

            return results;
        }
    }

    @Override
    protected void setAddParameters(PreparedStatement stmt, Question data) throws SQLException {
        stmt.setInt(1, data.getCourse().getId());
        stmt.setInt(2, data.getTopic().getId());
        stmt.setString(3, data.getQuestion());
    }

    @Override
    protected Question newData(ResultSet res) throws SQLException {
        val id = res.getInt("id");
        val question = res.getString("question");
        val course = courses.find(res.getInt("course_id"));
        val topic = topics.find(res.getInt("topic_id"));
        return new Question(id, course, topic, question);
    }

}
