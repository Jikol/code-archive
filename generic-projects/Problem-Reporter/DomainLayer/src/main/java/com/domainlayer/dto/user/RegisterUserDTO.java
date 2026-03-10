package com.domainlayer.dto.user;

import com.domainlayer.dto.SuperUserDTO;

public class RegisterUserDTO extends SuperUserDTO {
    public RegisterUserDTO(final String email, final String passwd, final String name, final String surname) {
        super(email, passwd, name, surname);
    }

    public String getEmail() { return email; }
    public String getPasswd() { return passwd; }
    public String getName() { return name; }
    public String getSurname() { return surname; }

    @Override
    public String toString() {
        return "RegisterUserDTO{" +
                "email='" + email + '\'' +
                ", passwd='" + passwd + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
