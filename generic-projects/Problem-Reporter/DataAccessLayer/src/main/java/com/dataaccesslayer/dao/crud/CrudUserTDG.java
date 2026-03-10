package com.dataaccesslayer.dao.crud;

import com.dataaccesslayer.Database;
import com.dataaccesslayer.dao.IUnitOfWork;
import com.dataaccesslayer.dao.mapper.UserMapper;
import com.dataaccesslayer.entity.ProblemEntity;
import com.dataaccesslayer.entity.UserEntity;

import java.sql.SQLException;
import java.util.*;

public class CrudUserTDG implements IUnitOfWork<UserEntity> {
    public final Map<String, List<UserEntity>> context = new HashMap<>();
    private final Database db = Database.getDatabase();
    private List<Integer> insertedIds = new ArrayList<>();

    public List<Integer> getInsertedIds() {
        return insertedIds;
    }

    @Override
    public void RegisterNew(final UserEntity entity) {
        Register(entity, INSERT);
    }

    @Override
    public void RegisterModified(final UserEntity entity) {
        Register(entity, MODIFY);
    }

    @Override
    public void RegisterDeleted(final UserEntity entity) {
        Register(entity, DELETE);
    }

    @Override
    public int Commit(final boolean makeDatabaseCommit) throws Exception {
        int executedStatements = 0;
        if (context == null || context.size() == 0) {
            return executedStatements;
        }
        try {
            db.BeginConnection();
            if (context.containsKey(INSERT)) {
                var simpleEntry = CommitInsert();
                executedStatements += (Integer) simpleEntry.getKey();
                insertedIds = (List) simpleEntry.getValue();
            }
            if (context.containsKey(MODIFY)) {
                executedStatements += CommitModify();
            }
            if (context.containsKey(DELETE)) {
                executedStatements += CommitDelete();
            }
            if (makeDatabaseCommit) {
                db.Commit();
                db.EndConnection();
            }
        } catch (SQLException ex) {
            db.Rollback();
            throw new Exception("SQL " + ex.getLocalizedMessage());
        }
        return executedStatements;
    }

    @Override
    public int Insert(final UserEntity userEntity) throws SQLException {
        String query = "INSERT INTO data.user (email, passwd, name, surname) VALUES (?, ?, ?, ?)";
        var parameters = new HashMap<>();
        parameters.put(1, new AbstractMap.SimpleEntry(String.class, userEntity.getEmail()));
        parameters.put(2, new AbstractMap.SimpleEntry(String.class, userEntity.getPasswd()));
        parameters.put(3, new AbstractMap.SimpleEntry(String.class, userEntity.getName()));
        parameters.put(4, new AbstractMap.SimpleEntry(String.class, userEntity.getSurname()));
        return db.ExecutePreparedUpdate(query, parameters);
    }

    @Override
    public int Update(final UserEntity userEntity) {
        return 0;
    }

    @Override
    public int Delete(final UserEntity userEntity) {
        return 0;
    }

    public UserEntity SelectByEmail(final String emailParam) throws Exception {
        db.BeginConnection();
        String query = "SELECT * FROM data.user WHERE email LIKE ?";
        var parameters = new HashMap<>();
        parameters.put(1, new AbstractMap.SimpleEntry(String.class, emailParam));
        return new UserMapper().mapResultSingle(db, db.ExecutePreparedSelect(query, parameters));
    }

    public List<UserEntity> SelectAll() throws Exception {
        db.BeginConnection();
        String query = "SELECT * FROM data.user";
        return new UserMapper().mapResultSet(db, db.ExecuteSelect(query));
    }

    private void Register(final UserEntity entity, final String operation) {
        List entitiesToPersistence = context.get(operation);
        if (entitiesToPersistence == null) {
            entitiesToPersistence = new ArrayList();
        }
        entitiesToPersistence.add(entity);
        context.put(operation, entitiesToPersistence);
    }

    private AbstractMap.SimpleEntry CommitInsert() throws SQLException {
        var objectsToBePersisted = context.get(INSERT);
        List<Integer> insertedIds = new ArrayList<>();
        int objectsInserted = 0;
        for (UserEntity entity : objectsToBePersisted) {
            insertedIds.add(Insert(entity));
            objectsInserted++;
        }
        return new AbstractMap.SimpleEntry(objectsInserted, insertedIds);
    }

    private int CommitModify() throws SQLException {
        var objectsToBePersisted = context.get(MODIFY);
        int objectsInserted = 0;
        for (UserEntity entity : objectsToBePersisted) {
            Update(entity);
            objectsInserted++;
        }
        return objectsInserted;
    }

    private int CommitDelete() throws SQLException {
        var objectsToBePersisted = context.get(DELETE);
        int objectsInserted = 0;
        for (UserEntity entity : objectsToBePersisted) {
            Delete(entity);
            objectsInserted++;
        }
        return objectsInserted;
    }
}
