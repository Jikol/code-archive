package com.domainlayer.dto.user;

import com.domainlayer.dto.SuperUserDTO;

public class UserDTO extends SuperUserDTO {
    public UserDTO(final String email, final String name, final String surname) {
        super(email, null, name, surname);
    }

    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
