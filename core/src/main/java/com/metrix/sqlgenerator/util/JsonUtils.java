package com.metrix.sqlgenerator.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.metrix.sqlgenerator.model.QueryRequest;
import com.metrix.sqlgenerator.model.OperatorType;
import com.metrix.sqlgenerator.util.OperatorTypeDeserializer;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OperatorType.class, new OperatorTypeDeserializer());
        mapper.registerModule(module);
    }

    public static QueryRequest parseQuery(String json) throws Exception {
        return mapper.readValue(json, QueryRequest.class);
    }
}
