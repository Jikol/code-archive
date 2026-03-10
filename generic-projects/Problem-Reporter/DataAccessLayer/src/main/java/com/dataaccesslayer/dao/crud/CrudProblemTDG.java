package com.dataaccesslayer.dao.crud;

import com.dataaccesslayer.Database;
import com.dataaccesslayer.dao.IUnitOfWork;
import com.dataaccesslayer.dao.mapper.ProblemMapper;
import com.dataaccesslayer.entity.ProblemEntity;

import java.sql.SQLException;
import java.util.*;

public class CrudProblemTDG implements IUnitOfWork<ProblemEntity> {
    public final Map<String, List<ProblemEntity>> context = new HashMap<>();
    private final Database db = Database.getDatabase();
    private List<Integer> insertedIds = new ArrayList<>();

    public List<Integer> getInsertedIds() {
        return insertedIds;
    }

    @Override
    public void RegisterNew(final ProblemEntity entity) {
        Register(entity, INSERT);
    }

    @Override
    public void RegisterModified(final ProblemEntity entity) {
        Register(entity, MODIFY);
    }

    @Override
    public void RegisterDeleted(final ProblemEntity entity) {
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
    public int Insert(final ProblemEntity entity) throws SQLException {
        String query = "INSERT INTO data.problem (title, summary, configuration, expect_behavior, actual_behavior, accepted, user_id, deployment_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        var parameters = new HashMap<>();
        parameters.put(1, new AbstractMap.SimpleEntry(String.class, entity.getTitle()));
        parameters.put(2, new AbstractMap.SimpleEntry(String.class, entity.getSummary()));
        parameters.put(3, new AbstractMap.SimpleEntry(String.class, entity.getConfiguration()));
        parameters.put(4, new AbstractMap.SimpleEntry(String.class, entity.getExpectedBehavior()));
        parameters.put(5, new AbstractMap.SimpleEntry(String.class, entity.getActualBehavior()));
        parameters.put(6, new AbstractMap.SimpleEntry(Boolean.class, entity.getAccepted()));
        parameters.put(7, new AbstractMap.SimpleEntry(Integer.class, entity.getUserEntity().getId()));
        parameters.put(8, new AbstractMap.SimpleEntry(Integer.class, entity.getDeploymentEntity().getId()));
        return db.ExecutePreparedUpdate(query, parameters);
    }

    @Override
    public int Update(final ProblemEntity entity) throws SQLException {
        String query = "UPDATE data.problem\n" +
                "SET title = ?, summary = ?, configuration = ?, expect_behavior = ?, actual_behavior = ?, accepted = ?, user_id = ?, deployment_id = ?\n" +
                "WHERE id = ?";
        var parameters = new HashMap<>();
        parameters.put(1, new AbstractMap.SimpleEntry(String.class, entity.getTitle()));
        parameters.put(2, new AbstractMap.SimpleEntry(String.class, entity.getSummary()));
        parameters.put(3, new AbstractMap.SimpleEntry(String.class, entity.getConfiguration()));
        parameters.put(4, new AbstractMap.SimpleEntry(String.class, entity.getExpectedBehavior()));
        parameters.put(5, new AbstractMap.SimpleEntry(String.class, entity.getActualBehavior()));
        parameters.put(6, new AbstractMap.SimpleEntry(Boolean.class, entity.getAccepted()));
        parameters.put(7, new AbstractMap.SimpleEntry(Integer.class, entity.getUserEntity().getId()));
        parameters.put(8, new AbstractMap.SimpleEntry(Integer.class, entity.getDeploymentEntity().getId()));
        parameters.put(9, new AbstractMap.SimpleEntry(Integer.class, entity.getId()));
        return db.ExecutePreparedUpdate(query, parameters);
    }

    @Override
    public int Delete(final ProblemEntity entity) throws SQLException {
        return 0;
    }

    public ProblemEntity SelectUnacceptedById(final Integer idParam) throws Exception {
        db.BeginConnection();
        String query = "SELECT *\n" +
                "FROM data.problem\n" +
                "    INNER JOIN data.\"user\" ON problem.user_id = \"user\".id\n" +
                "    INNER JOIN data.deployment ON problem.deployment_id = deployment.id\n" +
                "WHERE problem.id = ? AND accepted = false";
        var parameters = new HashMap<>();
        parameters.put(1, new AbstractMap.SimpleEntry(Integer.class, idParam));
        return new ProblemMapper().mapResultSingle(db, db.ExecutePreparedSelect(query, parameters));
    }

    public ProblemEntity SelectById(final Integer idParam) throws Exception {
        db.BeginConnection();
        String query = "SELECT *\n" +
                "FROM data.problem\n" +
                "         INNER JOIN data.\"user\" ON problem.user_id = \"user\".id\n" +
                "         INNER JOIN data.deployment ON problem.deployment_id = deployment.id\n" +
                "WHERE problem.id = ? AND accepted = true";
        var parameters = new HashMap<>();
        parameters.put(1, new AbstractMap.SimpleEntry(Integer.class, idParam));
        return new ProblemMapper().mapResultSingle(db, db.ExecutePreparedSelect(query, parameters));
    }

    public List<ProblemEntity> SelectAll(final String emailParam) throws Exception {
        db.BeginConnection();
        String query = "SELECT *\n" +
                "FROM data.problem\n" +
                "         INNER JOIN data.\"user\" ON problem.user_id = \"user\".id\n" +
                "         INNER JOIN data.deployment ON problem.deployment_id = deployment.id\n" +
                "WHERE email = ? AND accepted = true";
        var parameters = new HashMap<>();
        parameters.put(1, new AbstractMap.SimpleEntry(String.class, emailParam));
        return new ProblemMapper().mapResultSet(db, db.ExecutePreparedSelect(query, parameters));
    }

    public List<ProblemEntity> SelectAllUnresolved() throws Exception {
        db.BeginConnection();
        String query = "SELECT *\n" +
                "FROM data.problem\n" +
                "    LEFT JOIN data.solution ON problem.id = solution.problem_id\n" +
                "WHERE (problem_id IS NULL OR end_date IS NULL) AND accepted = true";
        return new ProblemMapper().mapResultSet(db, db.ExecuteSelect(query));
    }

    public List<ProblemEntity> SelectAllUnaccepted() throws Exception {
        db.BeginConnection();
        String query = "SELECT *\n" +
                "FROM data.problem\n" +
                "    INNER JOIN data.\"user\" ON problem.user_id = \"user\".id\n" +
                "    INNER JOIN data.deployment ON problem.deployment_id = deployment.id\n" +
                "WHERE accepted = false";
        return new ProblemMapper().mapResultSet(db, db.ExecuteSelect(query));
    }

    private void Register(final ProblemEntity entity, final String operation) {
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
        for (ProblemEntity entity : objectsToBePersisted) {
            insertedIds.add(Insert(entity));
            objectsInserted++;
        }
        return new AbstractMap.SimpleEntry(objectsInserted, insertedIds);
    }

    private int CommitModify() throws SQLException {
        var objectsToBePersisted = context.get(MODIFY);
        int objectsInserted = 0;
        for (ProblemEntity entity : objectsToBePersisted) {
            Update(entity);
            objectsInserted++;
        }
        return objectsInserted;
    }

    private int CommitDelete() throws SQLException {
        var objectsToBePersisted = context.get(DELETE);
        int objectsInserted = 0;
        for (ProblemEntity entity : objectsToBePersisted) {
            Delete(entity);
            objectsInserted++;
        }
        return objectsInserted;
    }
}
