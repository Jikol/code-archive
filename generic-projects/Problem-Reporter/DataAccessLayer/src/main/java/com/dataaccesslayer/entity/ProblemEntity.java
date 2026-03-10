package com.dataaccesslayer.entity;

import java.util.List;

public class ProblemEntity {
    private Integer id;
    private String title;
    private String summary;
    private String configuration;
    private String expectedBehavior;
    private String actualBehavior;
    private Boolean accepted;
    private UserEntity userEntity;
    private DeploymentEntity deploymentEntity;

    public ProblemEntity(final int id, final String title, final String summary, final String configuration, final String expectedBehavior,
                         final String actualBehavior, final UserEntity userEntity, DeploymentEntity deploymentEntity) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.configuration = configuration;
        this.expectedBehavior = expectedBehavior;
        this.actualBehavior = actualBehavior;
        this.userEntity = userEntity;
        this.deploymentEntity = deploymentEntity;
    }

    public ProblemEntity(final String title, final String summary, final String configuration, final String expectedBehavior,
                         final String actualBehavior, final Boolean accepted, final UserEntity userEntity, final DeploymentEntity deploymentEntity) {
        this.title = title;
        this.summary = summary;
        this.configuration = configuration;
        this.expectedBehavior = expectedBehavior;
        this.actualBehavior = actualBehavior;
        this.userEntity = userEntity;
        this.accepted = accepted;
        this.deploymentEntity = deploymentEntity;
    }

    public ProblemEntity(final Integer id, final String title, final String summary, final String configuration,
                         final String expectedBehavior, final String actualBehavior) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.configuration = configuration;
        this.expectedBehavior = expectedBehavior;
        this.actualBehavior = actualBehavior;
    }

    public ProblemEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public String getConfiguration() { return configuration; }
    public String getExpectedBehavior() { return expectedBehavior; }
    public String getActualBehavior() { return actualBehavior; }
    public Boolean getAccepted() { return accepted; }
    public UserEntity getUserEntity() { return userEntity; }
    public DeploymentEntity getDeploymentEntity() { return deploymentEntity; }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
    public void setExpectedBehavior(String expectedBehavior) {
        this.expectedBehavior = expectedBehavior;
    }
    public void setActualBehavior(String actualBehavior) {
        this.actualBehavior = actualBehavior;
    }
    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public String toString() {
        return "ProblemEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", configuration='" + configuration + '\'' +
                ", expectedBehavior='" + expectedBehavior + '\'' +
                ", actualBehavior='" + actualBehavior + '\'' +
                ", accepted=" + accepted +
                ", userEntity=" + userEntity +
                ", deploymentEntity=" + deploymentEntity +
                '}';
    }
}
