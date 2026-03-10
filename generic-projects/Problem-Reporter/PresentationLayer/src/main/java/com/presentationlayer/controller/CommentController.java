package com.presentationlayer.controller;

import com.domainlayer.CommentTM;
import com.domainlayer.ProblemTM;
import com.domainlayer.dto.comment.NewCommentDTO;
import com.presentationlayer.module.JsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/problem/comment")
public class CommentController {

    @PostMapping("/new")
    public ResponseEntity<String> postComment(
            @RequestHeader("Authorization") String token, @RequestBody NewCommentDTO comment) {
        Map callback = new CommentTM().PostComment(token, comment);
        return new ResponseEntity(JsonBuilder.BuildResponseJson(callback),
                HttpStatus.valueOf((Integer) callback.get("status"))
        );
    }
}
