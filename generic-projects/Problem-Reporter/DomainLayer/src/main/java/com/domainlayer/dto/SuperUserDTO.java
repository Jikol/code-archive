package com.domainlayer.dto;

import java.util.List;

public class SuperUserDTO {
    protected final String email;
    protected final String passwd;
    protected final String name;
    protected final String surname;

    public SuperUserDTO(String email, String passwd, String name, String surname) {
        this.email = email;
        this.passwd = passwd;
        this.name = name;
        this.surname = surname;
    }
}
