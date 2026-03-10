package com.domainlayer;

import com.dataaccesslayer.dao.crud.CrudUserTDG;
import com.dataaccesslayer.entity.UserEntity;
import com.domainlayer.dto.user.AuthenicateUserDTO;
import com.domainlayer.dto.user.RegisterUserDTO;
import com.domainlayer.dto.user.UserDTO;
import com.domainlayer.module.JwtException;
import com.domainlayer.module.JwtToken;
import com.domainlayer.module.PasswordEncryption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserTM {
    private int userRegistered = 0;
    private List<Integer> userRegisteredIds = new ArrayList<>();

    public int GetRegisteredUser() {
        return userRegistered;
    }

    public List<Integer> getUserRegisteredIds() {
        return userRegisteredIds;
    }

    public Map<Object, Object> RegisterUser(final RegisterUserDTO registerUserDTO, final boolean commit) {
        if (registerUserDTO.getEmail() == null || registerUserDTO.getPasswd() == null) {
            return Map.of(
                    "status", 400,
                    "error", "Email and Password are mandatory attributes"
            );
        }
        CrudUserTDG crudUserTDG = new CrudUserTDG();
        String hashedPassword = PasswordEncryption.GetHashedPassword(registerUserDTO.getPasswd());
        crudUserTDG.RegisterNew(new UserEntity(registerUserDTO.getEmail(), hashedPassword, registerUserDTO.getName(), registerUserDTO.getSurname()));
        try {
            userRegistered += crudUserTDG.Commit(commit);
        } catch (Exception ex) {
            return Map.of(
                    "status", 409,
                    "error", ex.getLocalizedMessage()
            );
        }
        userRegisteredIds = crudUserTDG.getInsertedIds();
        String token = JwtToken.GenerateToken(registerUserDTO.getEmail(), hashedPassword);
        return Map.of(
                "status", 201,
                "created", userRegistered,
                "token", token
        );
    }

    public Map<Object, Object> RegisterUsers(final List<RegisterUserDTO> registerUserDTOS) {
        CrudUserTDG crudUserTDG = new CrudUserTDG();
        AtomicBoolean problemFound = new AtomicBoolean(false);
        registerUserDTOS.forEach(registerUserDTO -> {
            if (registerUserDTO.getEmail() != null && registerUserDTO.getPasswd() != null) {
                crudUserTDG.RegisterNew(new UserEntity(registerUserDTO.getEmail(), registerUserDTO.getPasswd(),
                        registerUserDTO.getName(), registerUserDTO.getSurname()));
            } else {
                problemFound.set(true);
            }
        });
        try {
            userRegistered += crudUserTDG.Commit(true);
        } catch (Exception ex) {
            return Map.of(
                    "status", 409,
                    "error", ex.getLocalizedMessage()
            );
        }
        if (problemFound.get()) {
            return Map.of(
                    "status", 400,
                    "error", "Email and Password are mandatory attributes"
            );
        }
        return Map.of(
                "status", 201
        );
    }

    public Map<Object, Object> AuthenticateUser(final AuthenicateUserDTO authenicateUserDTO) {
        if (authenicateUserDTO.getEmail() == null || authenicateUserDTO.getPasswd() == null) {
            return Map.of(
                    "status", 400,
                    "error", "Email and Password are mandatory attributes"
            );
        }
        UserEntity userEntity = null;
        try {
            userEntity = new CrudUserTDG().SelectByEmail(authenicateUserDTO.getEmail());
        } catch (Exception ex) {
            return Map.of(
                    "status", 404,
                    "error", ex.getLocalizedMessage()
            );
        }
        if (PasswordEncryption.AuthenticatePassword(authenicateUserDTO.getPasswd(), userEntity.getPasswd())) {
            String token = JwtToken.GenerateToken(userEntity.getEmail(), userEntity.getPasswd());
            return Map.of(
                    "status", 200,
                    "token", token
            );
        } else {
            return Map.of(
                    "status", 403,
                    "error", "Incorrect email or password"
            );
        }
    }

    public Object ListAllUsers(final String token) {
        try {
            JwtToken.ValidateToken(token.replace("Bearer ", ""));
        } catch (JwtException ex) {
            return ex.getMyMessage();
        }
        List<UserEntity> userEntities = new ArrayList<>();
        try {
            userEntities = new CrudUserTDG().SelectAll();
        } catch (Exception ex) {
            return Map.of(
                    "status", 404,
                    "error", ex.getLocalizedMessage()
            );
        }
        List<UserDTO> userDTOList = new ArrayList<>();
        userEntities.forEach(userEntity -> {
            userDTOList.add(new UserDTO(userEntity.getEmail(), userEntity.getName(), userEntity.getSurname()));
        });
        return (List<UserDTO>) userDTOList;
    }
}
