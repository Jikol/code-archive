package com.domainlayer.dto.deployment;

public class DeploymentDTO {
    private final String domain;
    private final String contact;
    private final String desc;

    public DeploymentDTO(String domain, String contact, String desc) {
        this.domain = domain;
        this.contact = contact;
        this.desc = desc;
    }

    public String getDomain() {
        return domain;
    }

    public String getContact() {
        return contact;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "DeploymentDTO{" +
                "domain='" + domain + '\'' +
                ", contact='" + contact + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
