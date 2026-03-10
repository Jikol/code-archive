package com.dataaccesslayer.dao.crud;

import com.dataaccesslayer.Database;
import com.dataaccesslayer.dao.mapper.EmployeeMapper;
import com.dataaccesslayer.entity.EmployeeEntity;

import java.util.AbstractMap;
import java.util.HashMap;

public class CrudEmployeeTDG {
    private final Database db = Database.getDatabase();

    public EmployeeEntity SelectByEmail(final String emailParam) throws Exception {
        db.BeginConnection();
        String query = "SELECT * FROM data.employee WHERE email LIKE ?";
        var parameters = new HashMap<>();
        parameters.put(1, new AbstractMap.SimpleEntry(String.class, emailParam));
        return new EmployeeMapper().mapResultSingle(db, db.ExecutePreparedSelect(query, parameters));
    }
}
