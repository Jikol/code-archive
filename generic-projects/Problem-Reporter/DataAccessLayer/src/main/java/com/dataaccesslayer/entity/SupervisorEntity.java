package com.dataaccesslayer.entity;

import java.util.Date;
import java.util.List;

public class SupervisorEntity {
    private Integer id;
    private String email;
    private String passwd;
    private String name;
    private String surname;
    private Date entryDate;
    private Date terminationDate;
    private List<EmployeeEntity> employeeEntities;

    public SupervisorEntity(Integer id, String email, String passwd, String name, String surname, Date entryDate,
                            Date terminationDate, List<EmployeeEntity> employeeEntities) {
        this.id = id;
        this.email = email;
        this.passwd = passwd;
        this.name = name;
        this.surname = surname;
        this.entryDate = entryDate;
        this.terminationDate = terminationDate;
        this.employeeEntities = employeeEntities;
    }

    public SupervisorEntity(String email, String passwd, String name, String surname, Date entryDate,
                            Date terminationDate, List<EmployeeEntity> employeeEntities) {
        this.email = email;
        this.passwd = passwd;
        this.name = name;
        this.surname = surname;
        this.entryDate = entryDate;
        this.terminationDate = terminationDate;
        this.employeeEntities = employeeEntities;
    }

    public SupervisorEntity(String email, String passwd, String name, String surname, Date entryDate, Date terminationDate) {
        this.email = email;
        this.passwd = passwd;
        this.name = name;
        this.surname = surname;
        this.entryDate = entryDate;
        this.terminationDate = terminationDate;
    }

    public SupervisorEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getPasswd() {
        return passwd;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public Date getEntryDate() {
        return entryDate;
    }
    public Date getTerminationDate() {
        return terminationDate;
    }
    public List<EmployeeEntity> getEmployeeEntities() {
        return employeeEntities;
    }

    @Override
    public String toString() {
        return "SupervisorEntity{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", passwd='" + passwd + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", entryDate=" + entryDate +
                ", terminationDate=" + terminationDate +
                ", employeeEntities=" + employeeEntities +
                '}';
    }
}
