package toilari.questionapp.data;

import java.sql.SQLException;

import lombok.val;
import toilari.questionapp.database.Database;

/**
 * CourseDao
 */
public class CourseDao extends NamedQuestionFieldDao<Course> {
    public CourseDao(Database db) {
        super(db, "Course", Course::new);
    }

    /**
     * Removes the course and all of its questions.
     * 
     * @param id        ID of the course to remove
     * @param questions Question DAO to use for removing questions
     * @throws SQLException If removing questions (via
     *                      {@link QuestionDao#removeAllFromCourse(Integer)}), the
     *                      database connection or the DELETE-query fails
     */
    @Override
    public void remove(Integer id, QuestionDao questions, AnswerDao answers) throws SQLException {
        val course = find(id);
        course.getQuestions(questions).forEach((q) -> {
            try {
                questions.remove(q.getId(), answers);
            } catch (SQLException ignored) {
            }
        });
        super.remove(id, questions, answers);
    }
}
