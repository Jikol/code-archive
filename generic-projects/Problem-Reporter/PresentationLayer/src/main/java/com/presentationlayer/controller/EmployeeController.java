package com.presentationlayer.controller;

import com.domainlayer.EmployeeTM;
import com.domainlayer.dto.employee.AuthenticateEmployeeDTO;
import com.presentationlayer.module.JsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/staff")
public class EmployeeController {

    @PostMapping("/auth")
    public ResponseEntity<String> staffAuthenticate(@RequestBody AuthenticateEmployeeDTO employee) {
        Map callback = new EmployeeTM().AuthenticateStaff(employee);
        return new ResponseEntity(JsonBuilder.BuildResponseJson(callback),
                HttpStatus.valueOf((Integer) callback.get("status"))
        );
    }
}
