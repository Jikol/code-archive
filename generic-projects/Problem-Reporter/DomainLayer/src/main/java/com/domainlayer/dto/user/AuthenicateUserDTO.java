package com.domainlayer.dto.user;

import com.domainlayer.dto.SuperUserDTO;

public class AuthenicateUserDTO extends SuperUserDTO {
    public AuthenicateUserDTO(String email, String passwd) {
        super(email, passwd, null, null);
    }

    public String getEmail() {
        return email;
    }
    public String getPasswd() {
        return passwd;
    }

    @Override
    public String toString() {
        return "AuthenticateUserDTO{" +
                "email='" + email + '\'' +
                ", password='" + passwd + '\'' +
                '}';
    }
}
