package com.dataaccesslayer.entity;

public class UserCommentEntity {
    private Integer userId;
    private Integer commentId;

    public UserCommentEntity(Integer userId, Integer commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }

    public Integer getUserId() {
        return userId;
    }
    public Integer getCommentId() {
        return commentId;
    }

    @Override
    public String toString() {
        return "UserCommentEntity{" +
                "userId=" + userId +
                ", commentId=" + commentId +
                '}';
    }
}
