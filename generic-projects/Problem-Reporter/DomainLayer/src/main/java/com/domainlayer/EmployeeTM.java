package com.domainlayer;

import com.dataaccesslayer.dao.crud.CrudEmployeeTDG;
import com.dataaccesslayer.entity.EmployeeEntity;
import com.domainlayer.dto.employee.AuthenticateEmployeeDTO;
import com.domainlayer.module.JwtToken;
import com.domainlayer.module.PasswordEncryption;

import java.util.Map;

public class EmployeeTM {

    public Map<Object, Object> AuthenticateStaff(final AuthenticateEmployeeDTO authenticateEmployeeDTO) {
        if (authenticateEmployeeDTO.getEmail() == null || authenticateEmployeeDTO.getPasswd() == null) {
            return Map.of(
                    "status", 400,
                    "error", "Email and password are mandatory attributes"
            );
        }
        EmployeeEntity employeeEntity = null;
        try {
            employeeEntity = new CrudEmployeeTDG().SelectByEmail(authenticateEmployeeDTO.getEmail());
        } catch (Exception ex) {
            return Map.of(
                    "status", 404,
                    "error", ex.getLocalizedMessage()
            );
        }
        if (PasswordEncryption.AuthenticatePassword(authenticateEmployeeDTO.getPasswd(), employeeEntity.getPasswd())) {
            String token = JwtToken.GenerateToken(employeeEntity.getEmail(), employeeEntity.getPasswd());
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
}
