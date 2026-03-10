package com.presentationlayer.controller;

import com.domainlayer.UserTM;
import com.domainlayer.dto.user.AuthenicateUserDTO;
import com.domainlayer.dto.user.RegisterUserDTO;
import com.presentationlayer.module.JsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<String> userRegister(@RequestBody RegisterUserDTO user) {
        Map callback = new UserTM().RegisterUser(user, true);
        return new ResponseEntity(JsonBuilder.BuildResponseJson(callback),
                HttpStatus.valueOf((Integer) callback.get("status"))
        );
    }

    @PostMapping("/auth")
    public ResponseEntity<String> userAuthenticate(@RequestBody AuthenicateUserDTO user) {
        Map callback = new UserTM().AuthenticateUser(user);
        return new ResponseEntity(JsonBuilder.BuildResponseJson(callback),
                HttpStatus.valueOf((Integer) callback.get("status"))
        );
    }
}
