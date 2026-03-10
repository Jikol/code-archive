package com.dataaccesslayer.dao.mapper;

import com.dataaccesslayer.dao.AbstractRowMapper;
import com.dataaccesslayer.entity.AttachmentEntity;
import com.dataaccesslayer.entity.ProblemEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AttachmentMapper extends AbstractRowMapper<AttachmentEntity> {

    public AttachmentMapper() {
        super(AttachmentEntity.class);
    }

    @Override
    protected AttachmentEntity mapRow(ResultSet rs) throws Exception {
        try {
            int id = rs.getInt("id");
            byte[] data = rs.getBytes("data");
            return new AttachmentEntity(id, data);
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
    }

}
