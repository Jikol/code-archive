package com.dataaccesslayer.dao.crud;

import com.dataaccesslayer.Database;
import com.dataaccesslayer.dao.IUnitOfWork;
import com.dataaccesslayer.entity.ProblemEntity;
import com.dataaccesslayer.entity.SolutionEntity;

import java.sql.SQLException;
import java.util.*;

public class CrudSolutionTDG implements IUnitOfWork<SolutionEntity> {
    public final Map<String, List<SolutionEntity>> context = new HashMap<>();
    private final Database db = Database.getDatabase();
    private List<Integer> insertedIds = new ArrayList<>();

    public List<Integer> getInsertedIds() {
        return insertedIds;
    }

    @Override
    public void RegisterNew(SolutionEntity entity) {
        Register(entity, INSERT);
    }

    @Override
    public void RegisterModified(SolutionEntity entity) {
        Register(entity, INSERT);
    }

    @Override
    public void RegisterDeleted(SolutionEntity entity) {
        Register(entity, INSERT);
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
    public int Insert(SolutionEntity entity) throws SQLException {

        return 0;
    }

    @Override
    public int Update(SolutionEntity entity) throws SQLException {
        return 0;
    }

    @Override
    public int Delete(SolutionEntity entity) throws SQLException {
        return 0;
    }

    private void Register(final SolutionEntity entity, final String operation) {
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
        for (SolutionEntity entity : objectsToBePersisted) {
            insertedIds.add(Insert(entity));
            objectsInserted++;
        }
        return new AbstractMap.SimpleEntry(objectsInserted, insertedIds);
    }

    private int CommitModify() throws SQLException {
        var objectsToBePersisted = context.get(MODIFY);
        int objectsInserted = 0;
        for (SolutionEntity entity : objectsToBePersisted) {
            Update(entity);
            objectsInserted++;
        }
        return objectsInserted;
    }

    private int CommitDelete() throws SQLException {
        var objectsToBePersisted = context.get(DELETE);
        int objectsInserted = 0;
        for (SolutionEntity entity : objectsToBePersisted) {
            Delete(entity);
            objectsInserted++;
        }
        return objectsInserted;
    }
}
