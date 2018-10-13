package toilari.questionapp.data;

import java.sql.SQLException;
import java.util.List;

/**
 * Topic
 */
public class Topic extends NamedQuestionField {
    @Override
    public List<Question> getQuestions(QuestionDao dao) throws SQLException  {
        return dao.getQuestionsForTopic(getId());
    }

    public String getDeleteUrl() {
        return "/delete/topic/" + getId();
    }

    public String getInfoUrl() {
        return "/topics/" + getId();
    }

    public Topic(int id, String name) {
        super(id, name);
    }

    public Topic(String name) {
        // Call AllArgsConstructor
        super(name);
    }
}
