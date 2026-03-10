package com.domainlayer.module;

import java.util.Map;

public class JwtException extends RuntimeException {
    private Map<Object, Object> message;

    public JwtException(Map message) {
        this.message = message;
    }

    public Map getMyMessage() {
        return message;
    }
}
