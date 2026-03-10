package com.dataaccesslayer.entity;

public class EmployeeCommentEntity {
    private Integer employeeId;
    private Integer commentId;

    public EmployeeCommentEntity(Integer employeeId, Integer commentId) {
        this.employeeId = employeeId;
        this.commentId = commentId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }
    public Integer getCommentId() {
        return commentId;
    }

    @Override
    public String toString() {
        return "EmployeeCommentEntity{" +
                "employeeId=" + employeeId +
                ", commentId=" + commentId +
                '}';
    }
}
