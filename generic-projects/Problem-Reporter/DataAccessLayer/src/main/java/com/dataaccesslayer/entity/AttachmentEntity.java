package com.dataaccesslayer.entity;

public class AttachmentEntity {
    private Integer id;
    private byte[] data;
    private ProblemEntity problemEntity;

    public AttachmentEntity(Integer id, byte[] data, ProblemEntity problemEntity) {
        this.id = id;
        this.data = data;
        this.problemEntity = problemEntity;
    }

    public AttachmentEntity(byte[] data, ProblemEntity problemEntity) {
        this.data = data;
        this.problemEntity = problemEntity;
    }

    public AttachmentEntity(Integer id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    public AttachmentEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    public byte[] getData() {
        return data;
    }
    public ProblemEntity getProblemEntity() {
        return problemEntity;
    }

    @Override
    public String toString() {
        return "AttachmentEntity{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", problemEntity=" + problemEntity +
                '}';
    }
}
