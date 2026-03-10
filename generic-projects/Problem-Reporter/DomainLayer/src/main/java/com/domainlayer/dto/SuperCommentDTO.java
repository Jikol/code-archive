package com.domainlayer.dto;

import java.util.Date;

public class SuperCommentDTO {
    protected final String content;
    protected final Date created;
    protected final Date edited;

    public SuperCommentDTO(String content, Date created, Date edited) {
        this.content = content;
        this.created = created;
        this.edited = edited;
    }
}
