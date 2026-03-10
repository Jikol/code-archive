package com.dataaccesslayer.entity;

import java.util.Date;

public class SolutionEntity {
    private Integer id;
    private Date startDate;
    private Date endDate;
    private Integer changedSolvers;
    private ProblemEntity problemEntity;
    private EmployeeEntity employeeEntity;

    public SolutionEntity(Integer id, Date startDate, Date endDate, Integer changedSolvers,
                          ProblemEntity problemEntity, EmployeeEntity employeeEntity) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.changedSolvers = changedSolvers;
        this.problemEntity = problemEntity;
        this.employeeEntity = employeeEntity;
    }

    public SolutionEntity(Date startDate, Date endDate, Integer changedSolvers,
                          ProblemEntity problemEntity, EmployeeEntity employeeEntity) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.changedSolvers = changedSolvers;
        this.problemEntity = problemEntity;
        this.employeeEntity = employeeEntity;
    }

    public SolutionEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    public Date getStartDate() {
        return startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public Integer getChangedSolvers() {
        return changedSolvers;
    }
    public ProblemEntity getProblemEntity() {
        return problemEntity;
    }
    public EmployeeEntity getEmployeeEntity() {
        return employeeEntity;
    }

    @Override
    public String toString() {
        return "SolutionEntity{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", changedSolvers=" + changedSolvers +
                ", problemEntity=" + problemEntity +
                ", employeeEntity=" + employeeEntity +
                '}';
    }
}
