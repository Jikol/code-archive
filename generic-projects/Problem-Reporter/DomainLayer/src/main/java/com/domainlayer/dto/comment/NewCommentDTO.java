package com.domainlayer.dto.comment;

import com.domainlayer.dto.SuperCommentDTO;

import java.util.Date;

public class NewCommentDTO extends SuperCommentDTO {
    private final Integer problemId;

    public NewCommentDTO(final String content, final Integer problem) {
        super(content, null, null);
        this.problemId = problem;
    }

    public String getContent() {
        return content;
    }
    public Integer getProblemId() {
        return problemId;
    }

    @Override
    public String toString() {
        return "NewCommentDTO{" +
                "content='" + content + '\'' +
                ", created=" + created +
                ", edited=" + edited +
                '}';
    }
}
