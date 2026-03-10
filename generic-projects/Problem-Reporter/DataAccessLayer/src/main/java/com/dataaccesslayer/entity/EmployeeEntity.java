package com.dataaccesslayer.entity;

import java.util.Date;

public class EmployeeEntity {
    private Integer id;
    private String email;
    private String passwd;
    private String name;
    private String surname;
    private Date entryDate;
    private Date terminationDate;

    public EmployeeEntity(Integer id, String email, String passwd, String name, String surname, Date entryDate, Date terminationDate) {
        this.id = id;
        this.email = email;
        this.passwd = passwd;
        this.name = name;
        this.surname = surname;
        this.entryDate = entryDate;
        this.terminationDate = terminationDate;
    }

    public EmployeeEntity(String email, String passwd, String name, String surname, Date entryDate, Date terminationDate) {
        this.email = email;
        this.passwd = passwd;
        this.name = name;
        this.surname = surname;
        this.entryDate = entryDate;
        this.terminationDate = terminationDate;
    }

    public EmployeeEntity(Integer id) {
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

    @Override
    public String toString() {
        return "EmployeeEntity{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", passwd='" + passwd + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", entryDate=" + entryDate +
                ", terminationDate=" + terminationDate +
                '}';
    }
}

