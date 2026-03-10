package com.dataaccesslayer.dao.crud;

import com.dataaccesslayer.Database;
import com.dataaccesslayer.dao.IUnitOfWork;
import com.dataaccesslayer.dao.mapper.AttachmentMapper;
import com.dataaccesslayer.dao.mapper.DeploymentMapper;
import com.dataaccesslayer.entity.AttachmentEntity;
import com.dataaccesslayer.entity.DeploymentEntity;

import java.sql.SQLException;
import java.util.*;

public class CrudAttachmentTDG implements IUnitOfWork<AttachmentEntity> {
    public final Map<String, List<AttachmentEntity>> context = new HashMap<>();
    private final Database db = Database.getDatabase();
    private List<Integer> insertedIds = new ArrayList<>();

    @Override
    public void RegisterNew(final AttachmentEntity entity) {
        Register(entity, INSERT);
    }

    @Override
    public void RegisterModified(final AttachmentEntity entity) {
        Register(entity, MODIFY);
    }

    @Override
    public void RegisterDeleted(final AttachmentEntity entity) {
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
    public int Insert(final AttachmentEntity entity) throws SQLException {
        String query = "INSERT INTO data.attachment (data, problem_id) " +
                "VALUES (?, ?)";
        var parameters = new HashMap<>();
        parameters.put(1, new AbstractMap.SimpleEntry(byte[].class, entity.getData()));
        parameters.put(2, new AbstractMap.SimpleEntry(Integer.class, entity.getProblemEntity().getId()));
        return db.ExecutePreparedUpdate(query, parameters);
    }

    @Override
    public int Update(final AttachmentEntity entity) throws SQLException {
        return 0;
    }

    @Override
    public int Delete(final AttachmentEntity entity) throws SQLException {
        return 0;
    }

    public List<AttachmentEntity> SelectAllById(final int idParam) throws Exception {
        db.BeginConnection();
        String query = "SELECT * FROM data.attachment WHERE problem_id = ?";
        var parameters = new HashMap<>();
        parameters.put(1, new AbstractMap.SimpleEntry(Integer.class, idParam));
        return new AttachmentMapper().mapResultSet(db, db.ExecutePreparedSelect(query, parameters));
    }

    private void Register(final AttachmentEntity entity, final String operation) {
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
        for (AttachmentEntity entity : objectsToBePersisted) {
            insertedIds.add(Insert(entity));
            objectsInserted++;
        }
        return new AbstractMap.SimpleEntry(objectsInserted, insertedIds);
    }

    private int CommitModify() throws SQLException {
        var objectsToBePersisted = context.get(MODIFY);
        int objectsInserted = 0;
        for (AttachmentEntity entity : objectsToBePersisted) {
            Update(entity);
            objectsInserted++;
        }
        return objectsInserted;
    }

    private int CommitDelete() throws SQLException {
        var objectsToBePersisted = context.get(DELETE);
        int objectsInserted = 0;
        for (AttachmentEntity entity : objectsToBePersisted) {
            Delete(entity);
            objectsInserted++;
        }
        return objectsInserted;
    }
}
