package com.presentationlayer.controller;

import com.domainlayer.ProblemTM;
import com.domainlayer.dto.problem.NewProblemDTO;
import com.domainlayer.dto.user.RegisterUserDTO;
import com.presentationlayer.module.JsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/problem")
public class ProblemController {

    @PostMapping("/new/unregister")
    public ResponseEntity<String> reportProblemUnregister(@RequestBody Map requestProblem) {
        var problem = (Map) requestProblem.get("problem");
        var creds = (Map) requestProblem.get("creds");
        NewProblemDTO newProblemDTO = new NewProblemDTO(
                (String) problem.get("title"),
                (String) problem.get("summary"),
                (String) problem.get("configuration"),
                (String) problem.get("expectedBehavior"),
                (String) problem.get("actualBehavior"),
                (String) requestProblem.get("domain"),
                (List) requestProblem.get("attachments"),
                new RegisterUserDTO((String) creds.get("email"), (String) creds.get("passwd"),
                        (String) creds.get("name"), (String) creds.get("surname")
                )
        );
        Map callback = new ProblemTM().CreateNewProblem(newProblemDTO, null);
        return new ResponseEntity(JsonBuilder.BuildResponseJson(callback),
                HttpStatus.valueOf((Integer) callback.get("status"))
        );
    }

    @PostMapping("/new")
    public ResponseEntity<String> reportProblem(
            @RequestHeader("Authorization") String token, @RequestBody Map requestProblem) {
        var problem = (Map) requestProblem.get("problem");
        NewProblemDTO newProblemDTO = new NewProblemDTO(
                (String) problem.get("title"),
                (String) problem.get("summary"),
                (String) problem.get("configuration"),
                (String) problem.get("expectedBehavior"),
                (String) problem.get("actualBehavior"),
                (String) requestProblem.get("domain"),
                (List) requestProblem.get("attachments")
        );
        Map callback = new ProblemTM().CreateNewProblem(newProblemDTO, token);
        return new ResponseEntity(JsonBuilder.BuildResponseJson(callback),
                HttpStatus.valueOf((Integer) callback.get("status"))
        );
    }

    @GetMapping("/list/user")
    public Object listPersonalReportedProblems(@RequestHeader("Authorization") String token) {
        var response = new ProblemTM().ListAllUserProblems(token);
        if (response instanceof Map) {
            return new ResponseEntity<String>(JsonBuilder.BuildResponseJson((Map) response),
                    HttpStatus.valueOf((Integer) ((Map) response).get("status")));
        }
        if (response instanceof List) {
            return response;
        }
        return null;
    }

    @GetMapping("/list/unresolved")
    public Object listUnresolvedReportedProblems() {
        var response = new ProblemTM().ListUnresolvedProblems();
        if (response instanceof Map) {
            return new ResponseEntity<String>(JsonBuilder.BuildResponseJson((Map) response),
                    HttpStatus.valueOf((Integer) ((Map) response).get("status")));
        }
        if (response instanceof List) {
            return response;
        }
        return null;
    }
}
