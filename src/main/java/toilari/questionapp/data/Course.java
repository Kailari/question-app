package toilari.questionapp.data;

import java.sql.SQLException;
import java.util.List;

/**
 * Course
 */
public class Course extends NamedQuestionField {
    @Override
    public List<Question> getQuestions(QuestionDao dao) throws SQLException {
        return dao.getQuestionsForCourse(getId());
    }

    public String getDeleteUrl() {
        return "/delete/course/" + getId();
    }

    public String getInfoUrl() {
        return "/courses/" + getId();
    }

    public Course(int id, String name) {
        super(id, name);
    }

    public Course(String name) {
        super(name);
    }
}
