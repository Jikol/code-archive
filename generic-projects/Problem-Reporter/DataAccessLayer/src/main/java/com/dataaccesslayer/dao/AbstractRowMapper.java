package com.dataaccesslayer.dao;

import com.dataaccesslayer.Database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRowMapper<T> {
    private final Class<T> classType;

    public AbstractRowMapper(Class<T> classType) {
        this.classType = classType;
    }

    public List<T> mapResultSet(Database db, ResultSet rs) throws Exception {
        try {
            List<T> objectList = new ArrayList<>();
            while(rs.next()) {
                objectList.add(mapRow(rs));
            }
            rs.close();
            if (objectList.isEmpty()) {
                throw new Exception("no record found for the requested resource(s): " + classType.toString());
            }
            return objectList;
        } finally {
            db.EndConnection();
        }
    }

    public T mapResultSingle(Database db, ResultSet rs) throws Exception {
        try {
            T object = null;
            while(rs.next()) {
                object = mapRow(rs);
            }
            rs.close();
            if (object == null) {
                throw new Exception("no record found for the requested resource(s): " + classType.toString());
            }
            return object;
        } finally {
            db.EndConnection();
        }
    }

    protected abstract T mapRow(ResultSet rs) throws Exception;
}
