package com.domainlayer.dto;

import java.util.List;

public class SuperProblemDTO {
    protected final String title;
    protected final String summary;
    protected final String configuration;
    protected final String expectedBehavior;
    protected final String actualBehavior;
    protected final List<String> attachments;

    public SuperProblemDTO(String title, String summary, String configuration, String expectedBehavior,
                           String actualBehavior, List<String> attachments) {
        this.title = title;
        this.summary = summary;
        this.configuration = configuration;
        this.expectedBehavior = expectedBehavior;
        this.actualBehavior = actualBehavior;
        this.attachments = attachments;
    }
}
