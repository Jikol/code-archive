package com.dataaccesslayer.entity;

import java.util.Date;

public class CommentEntity {
    private Integer id;
    private String content;
    private Date createdDate;
    private Date editedDate;
    private ProblemEntity problemEntity;
    private Object posterEntity;

    public CommentEntity(Integer id, String content, Date createdDate, Date editedDate, ProblemEntity problemEntity, Object posterEntity) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.editedDate = editedDate;
        this.problemEntity = problemEntity;
        this.posterEntity = posterEntity;
    }

    public CommentEntity(String content, Date createdDate, Date editedDate, ProblemEntity problemEntity, Object posterEntity) {
        this.content = content;
        this.createdDate = createdDate;
        this.editedDate = editedDate;
        this.problemEntity = problemEntity;
        this.posterEntity = posterEntity;
    }

    public CommentEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    public String getContent() {
        return content;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public Date getEditedDate() {
        return editedDate;
    }
    public ProblemEntity getProblemEntity() {
        return problemEntity;
    }

    @Override
    public String toString() {
        return "CommentEntity{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", editedDate=" + editedDate +
                ", problemEntity=" + problemEntity +
                ", posterEntity=" + posterEntity +
                '}';
    }
}
