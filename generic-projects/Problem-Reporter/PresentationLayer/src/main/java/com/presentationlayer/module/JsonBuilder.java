package com.presentationlayer.module;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.Date;
import java.util.Map;

public class JsonBuilder {
    private JsonBuilder() {
        throw new AssertionError();
    }

    public static String BuildResponseJson(final Map message) {
        JsonObjectBuilder target = Json.createObjectBuilder()
                .add("timestamp", new Date(System.currentTimeMillis()).toString())
                .add("status", (Integer) message.get("status"));
        if (message.get("error") != null) {
            target.add("error", message.get("error").toString());
        }
        if (message.get("token") != null) {
            target.add("token", message.get("token").toString());
        }
        if (message.get("created") != null) {
            target.add("entitiesAffected", (Integer) message.get("created"));
        }
        return target.build().toString();
    }
}
