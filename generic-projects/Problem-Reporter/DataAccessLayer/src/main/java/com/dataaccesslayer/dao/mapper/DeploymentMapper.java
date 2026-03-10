package com.dataaccesslayer.dao.mapper;

import com.dataaccesslayer.dao.AbstractRowMapper;
import com.dataaccesslayer.entity.DeploymentEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeploymentMapper extends AbstractRowMapper<DeploymentEntity> {

    public DeploymentMapper() {
        super(DeploymentEntity.class);
    }

    @Override
    protected DeploymentEntity mapRow(ResultSet rs) throws Exception {
        try {
            int id = rs.getInt("id");
            String domain = rs.getString("domain");
            String contact = rs.getString("contact");
            String desc = rs.getString("desc");
            return new DeploymentEntity(id, domain, contact, desc);
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
    }
}
