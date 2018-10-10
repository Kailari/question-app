package toilari.questionapp.data;

import toilari.questionapp.database.Database;

/**
 * TopicDao
 */
public class TopicDao extends NamedQuestionFieldDao<Topic> {
    public TopicDao(Database db) {
        super(db, "Topic", Topic::new);
    }
}
