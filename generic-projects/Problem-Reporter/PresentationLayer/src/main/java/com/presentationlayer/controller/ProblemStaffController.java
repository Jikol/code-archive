package com.presentationlayer.controller;

import com.domainlayer.ProblemTM;
import com.presentationlayer.module.JsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/problem/staff")
public class ProblemStaffController {

    @GetMapping("/list/unaccepted")
    public Object listUnacceptedProblems(@RequestHeader("Authorization") String token) {
        var response = new ProblemTM().ListUnacceptedProblems(token);
        if (response instanceof Map) {
            return new ResponseEntity<String>(JsonBuilder.BuildResponseJson((Map) response),
                    HttpStatus.valueOf((Integer) ((Map) response).get("status")));
        }
        if (response instanceof List) {
            return response;
        }
        return null;
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptProblem(@RequestHeader("Authorization") String token, @RequestBody Map problem) {
        Integer problemId = (Integer) problem.get("problem");
        Map callback = new ProblemTM().AcceptProblem(token, problemId);
        return new ResponseEntity(JsonBuilder.BuildResponseJson(callback),
                HttpStatus.valueOf((Integer) callback.get("status"))
        );
    }

    @PostMapping("/close")
    public ResponseEntity<String> closeProblem(@RequestHeader("Authorization") String token, @RequestBody Map problem) {
        Integer problemId = (Integer) problem.get("problem");
        Map callback = new ProblemTM().CloseProblem(token, problemId);
        return new ResponseEntity(JsonBuilder.BuildResponseJson(callback),
                HttpStatus.valueOf((Integer) callback.get("status"))
        );
    }
}
