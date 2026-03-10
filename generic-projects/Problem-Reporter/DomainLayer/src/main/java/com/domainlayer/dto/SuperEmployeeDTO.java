package com.domainlayer.dto;

import java.util.Date;

public class SuperEmployeeDTO {
    protected final String email;
    protected final String passwd;
    protected final String name;
    protected final String surname;
    protected final Date entry_date;
    protected final Date termination_date;

    public SuperEmployeeDTO(String email, String passwd, String name, String surname,
                            Date entry_date, Date termination_date) {
        this.email = email;
        this.passwd = passwd;
        this.name = name;
        this.surname = surname;
        this.entry_date = entry_date;
        this.termination_date = termination_date;
    }
}
