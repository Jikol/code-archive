package com.dataaccesslayer.dao;

import java.sql.SQLException;
import java.util.*;

public interface IUnitOfWork<T> {
    public String INSERT = "INSERT";
    public String DELETE = "DELETE";
    public String MODIFY = "MODIFY";

    public void RegisterNew(T entity);
    public void RegisterModified(T entity);
    public void RegisterDeleted(T entity);
    public int Commit(final boolean control) throws Exception;

    public int Insert(T entity) throws SQLException;
    public int Update(T entity) throws SQLException;
    public int Delete(T entity) throws SQLException;
}
