package com.dataaccesslayer.dao.mapper;

import com.dataaccesslayer.dao.AbstractRowMapper;
import com.dataaccesslayer.entity.EmployeeEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class EmployeeMapper extends AbstractRowMapper<EmployeeEntity> {

    public EmployeeMapper() {
        super(EmployeeEntity.class);
    }

    @Override
    protected EmployeeEntity mapRow(ResultSet rs) throws Exception {
        try {
            int id = rs.getInt("id");
            String email = rs.getString("email");
            String passwd = rs.getString("passwd");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            Date entryDate = rs.getDate("entry_date");
            Date terminationDate = rs.getDate("termination_date");
            return new EmployeeEntity(id, email, passwd, name, surname, entryDate, terminationDate);
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
    }
}
