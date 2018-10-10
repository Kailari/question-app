package toilari.questionapp.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * Simple DAO for objects identified by integer primary key.
 */
@Slf4j
public abstract class SimpleIntPKDao<TData> implements IDao<TData, Integer> {
    @Getter(AccessLevel.PROTECTED)
    private final String tableName;
    @Getter(AccessLevel.PROTECTED)
    private final Database db;
    private final String[] fields;

    /**
     * Constructs a new DAO instance.
     * 
     * @param db        Database to read from
     * @param tableName Name of the database table to target
     * @param fields    Object fields, primary key (id) should be ommited
     */
    protected SimpleIntPKDao(Database db, String tableName, String[] fields) {
        this.db = db;
        this.tableName = tableName;
        this.fields = fields;
    }

    @Override
    public TData find(Integer id) throws SQLException {
        try (val connection = db.getConnection();
                val stmt = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE id=?")) {
            stmt.setInt(1, id);
            val result = stmt.executeQuery();

            if (!result.next()) {
                LOG.warn("No %s for id=%d could be found.", tableName, id);
                return null;
            }

            return newData(result);
        }
    }

    @Override
    public List<TData> findAll() throws SQLException {
        try (val connection = db.getConnection();
                val stmt = connection.prepareStatement("SELECT * FROM " + tableName)) {
            val result = stmt.executeQuery();

            val results = new ArrayList<TData>();
            while (result.next()) {
                results.add(newData(result));
            }

            return results;
        }
    }

    @Override
    public void add(TData data) throws SQLException {
        try (val connection = db.getConnection(); val stmt = connection.prepareStatement(constructAddQuery())) {
            setAddParameters(stmt, data);
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Integer id, TData data) throws SQLException {
        try (val connection = db.getConnection(); val stmt = connection.prepareStatement(constructUpdateQuery())) {
            setAddParameters(stmt, data);
            stmt.setInt(fields.length + 1, id);

            stmt.executeUpdate();
        }
    }

    protected abstract void setAddParameters(PreparedStatement stmt, TData data) throws SQLException;

    protected abstract TData newData(ResultSet res) throws SQLException;

    private String constructAddQuery() {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        builder.append(getTableName());
        builder.append(" (");

        for (int i = 0; i < fields.length - 1; i++) {
            builder.append(fields[i]);
            builder.append(',');
        }

        if (fields.length > 0) {
            builder.append(fields[fields.length - 1]);
        }

        builder.append(") VALUES (");

        for (int i = 0; i < fields.length - 1; i++) {
            builder.append("?,");
        }

        if (fields.length > 0) {
            builder.append('?');
        }
        builder.append(')');

        return builder.toString();
    }

    private String constructUpdateQuery() {
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ");
        builder.append(getTableName());
        builder.append(" SET ");

        for (int i = 0; i < fields.length - 1; i++) {
            builder.append(fields[i]);
            builder.append("=?,");
        }

        if (fields.length > 0) {
            builder.append(fields[fields.length - 1]);
            builder.append("=?");
        }

        builder.append(" WHERE id=?");

        return builder.toString();
    }
}