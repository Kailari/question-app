package toilari.questionapp.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public List<Question> getQuestionsForCourse(int id) {
        return null;
    }

    public List<Question> getQuestionsForTopic(int id) {
        return null;
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
