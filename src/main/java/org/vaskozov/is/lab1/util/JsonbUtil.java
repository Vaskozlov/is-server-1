package org.vaskozov.is.lab1.util;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;

public class JsonbUtil {
    private static final JsonbConfig JSONB_CONFIG = new JsonbConfig();
    public static final Jsonb JSONB = JsonbBuilder.create(JSONB_CONFIG);

    public static <T> T fromJson(String json, Class<T> clazz) throws JsonbException {
        return JSONB.fromJson(json, clazz);
    }

    public static String toJson(Object obj) {
        return JSONB.toJson(obj);
    }
}