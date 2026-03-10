package com.domainlayer.dto.employee;

import com.domainlayer.dto.SuperEmployeeDTO;

public class AuthenticateEmployeeDTO extends SuperEmployeeDTO {

    public AuthenticateEmployeeDTO(String email, String passwd) {
        super(email, passwd, null, null, null, null);
    }

    public String getEmail() {
        return email;
    }
    public String getPasswd() {
        return passwd;
    }

    @Override
    public String toString() {
        return "AuthenticateEmployeeDTO{" +
                "email='" + email + '\'' +
                ", passwd='" + passwd + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", entry_date='" + entry_date + '\'' +
                ", termination_date='" + termination_date + '\'' +
                '}';
    }
}
