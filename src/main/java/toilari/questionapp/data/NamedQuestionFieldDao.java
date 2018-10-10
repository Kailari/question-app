package toilari.questionapp.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.val;
import toilari.questionapp.database.Database;
import toilari.questionapp.database.SimpleIntPKDao;

/**
 * NamedQuestionFieldDao
 */
public abstract class NamedQuestionFieldDao<TData extends NamedQuestionField> extends SimpleIntPKDao<TData> {
    private static final String[] FIELDS = new String[] { "name" };

    private final DataConstructor<TData> ctor;

    protected NamedQuestionFieldDao(Database db, String tableName, DataConstructor<TData> ctor) {
        super(db, tableName, FIELDS);
        this.ctor = ctor;
    }

    @Override
    protected TData newData(ResultSet res) throws SQLException {
        val id = res.getInt("id");
        val name = res.getString("name");
        return ctor.newInstance(res, id, name);
    }

    @Override
    protected void setAddParameters(PreparedStatement stmt, TData data) throws SQLException {
        stmt.setString(1, data.getName());
    }

    protected interface DataConstructor<TData> {
        TData newInstance(int id, String name) throws SQLException;

        default TData newInstance(ResultSet res, int id, String name) throws SQLException {
            return newInstance(id, name);
        }
    }
}
