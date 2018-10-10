package toilari.questionapp.data;

import toilari.questionapp.database.Database;

/**
 * CourseDao
 */
public class CourseDao extends NamedQuestionFieldDao<Course> {
    public CourseDao(Database db) {
        super(db, "Course", Course::new);
    }
}
