package com.domainlayer.dto.problem;

import com.domainlayer.dto.SuperProblemDTO;
import com.domainlayer.dto.deployment.DeploymentDTO;
import com.domainlayer.dto.user.RegisterUserDTO;

import java.util.List;

public class NewProblemDTO extends SuperProblemDTO {
    private final RegisterUserDTO registerUserDTO;
    private final String deploymentDomain;

    public NewProblemDTO(String title, String summary, String configuration, String expectedBehavior,
                         String actualBehavior, String deploymentDomain, List attachments,
                         RegisterUserDTO registerUserDTO) {
        super(title, summary, configuration, expectedBehavior, actualBehavior, attachments);
        this.registerUserDTO = registerUserDTO;
        this.deploymentDomain = deploymentDomain;
    }

    public NewProblemDTO(String title, String summary, String configuration, String expectedBehavior,
                         String actualBehavior, String deploymentDomain, List attachments) {
        super(title, summary, configuration, expectedBehavior, actualBehavior, attachments);
        this.registerUserDTO = null;
        this.deploymentDomain = deploymentDomain;
    }

    public String getTitle() {
        return title;
    }
    public String getSummary() {
        return summary;
    }
    public String getConfiguration() {
        return configuration;
    }
    public String getExpectedBehavior() {
        return expectedBehavior;
    }
    public String getActualBehavior() {
        return actualBehavior;
    }
    public List getAttachments() {
        return attachments;
    }

    public String getDeploymentDomain() {
        return deploymentDomain;
    }
    public RegisterUserDTO getRegisterUserDTO() {
        return registerUserDTO;
    }

    @Override
    public String toString() {
        return "NewProblemDTO{" +
                "title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", configuration='" + configuration + '\'' +
                ", expectedBehavior='" + expectedBehavior + '\'' +
                ", actualBehavior='" + actualBehavior + '\'' +
                ", attachments=" + attachments +
                ", registerUserDTO=" + registerUserDTO +
                ", deploymentDomain='" + deploymentDomain + '\'' +
                '}';
    }
}
