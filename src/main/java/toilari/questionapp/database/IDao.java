package toilari.questionapp.database;

import java.sql.SQLException;
import java.util.List;

/**
 * Base-interface for Data Access Objects
 */
public interface IDao<TData, TId> {
    /**
     * Finds the entry matching the given ID.
     * 
     * @param id unique ID of the object to search for
     * @return The object with given ID. null if none are found.
     */
    TData find(TId id) throws SQLException;

    /**
     * Returns all entries of this type from the database.
     */
    List<TData> findAll() throws SQLException;

    /**
     * Adds given object to the database.
     * 
     * @param data Data to add
     */
    void add(TData data) throws SQLException;

    /**
     * Updates data in the database.
     * 
     * @param id ID of data to update
     * @param data new values to set
     */
    void update(TId id, TData data) throws SQLException;
}
