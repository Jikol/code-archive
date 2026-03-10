package com.domainlayer;

import com.dataaccesslayer.dao.crud.*;
import com.dataaccesslayer.entity.*;
import com.domainlayer.dto.comment.NewCommentDTO;
import com.domainlayer.module.JwtException;
import com.domainlayer.module.JwtToken;

import java.util.Date;
import java.util.Map;

public class CommentTM {
    private int commentCreated = 0;

    public int GetCreatedComments() {
        return commentCreated;
    }

    public Map<Object, Object> PostComment(final String token, final NewCommentDTO newCommentDTO) {
        String posterEmail = null;
        try {
            posterEmail = JwtToken.ValidateToken(token.replace("Bearer ", ""));
        } catch (JwtException ex) {
            return ex.getMyMessage();
        }
        if (newCommentDTO.getContent() == null || newCommentDTO.getProblemId() == null) {
            return Map.of(
                    "status", 400,
                    "error", "Comment must have defined content and corresponding problem identifier"
            );
        }
        ProblemEntity problemEntity = null;
        try {
            problemEntity = new CrudProblemTDG().SelectById(newCommentDTO.getProblemId());
        } catch (Exception ex) {
            return Map.of(
                    "status", 409,
                    "error", ex.getLocalizedMessage()
            );
        }
        Object poster = null;
        try {
            poster = (UserEntity) new CrudUserTDG().SelectByEmail(posterEmail);
        } catch (Exception ex1) {
            try {
                poster = (EmployeeEntity) new CrudEmployeeTDG().SelectByEmail(posterEmail);
            } catch (Exception ex2) {
                return Map.of(
                        "status", 404,
                        "error", "Provided email does not represent user or employee"
                );
            }
        }
        Date createdDate = new Date(System.currentTimeMillis());
        CrudCommentTDG crudCommentTDG = new CrudCommentTDG();
        crudCommentTDG.RegisterNew(new CommentEntity(
                newCommentDTO.getContent(),
                createdDate,
                null,
                problemEntity,
                poster
        ));
        try {
            commentCreated += crudCommentTDG.Commit(false);
        } catch (Exception ex) {
            return Map.of(
                    "status", 409,
                    "error", ex.getLocalizedMessage()
            );
        }
        CommentEntity commentEntity = new CommentEntity(
                crudCommentTDG.getInsertedIds().get(crudCommentTDG.getInsertedIds().size() - 1)
        );
        if (poster instanceof UserEntity) {
            CrudUserCommentTDG crudUserCommentTDG = new CrudUserCommentTDG();
            crudUserCommentTDG.RegisterNew(new UserCommentEntity(((UserEntity) poster).getId(), commentEntity.getId()));
            try {
                crudUserCommentTDG.Commit(true);
            } catch (Exception ex) {
                return Map.of(
                        "status", 409,
                        "error", ex.getLocalizedMessage()
                );
            }
        }
        if (poster instanceof EmployeeEntity) {
            CrudEmployeeCommnetTDG crudEmployeeCommnetTDG = new CrudEmployeeCommnetTDG();
            crudEmployeeCommnetTDG.RegisterNew(new EmployeeCommentEntity(((EmployeeEntity) poster).getId(), commentEntity.getId()));
            try {
                crudEmployeeCommnetTDG.Commit(true);
            } catch (Exception ex) {
                return Map.of(
                        "status", 409,
                        "error", ex.getLocalizedMessage()
                );
            }
        }
        return Map.of(
                "status", 201,
                "created", commentCreated
        );
    }
}
