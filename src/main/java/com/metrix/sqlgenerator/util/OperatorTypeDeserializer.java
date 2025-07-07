package com.metrix.sqlgenerator.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.metrix.sqlgenerator.model.OperatorType;
import java.io.IOException;

public class OperatorTypeDeserializer extends JsonDeserializer<OperatorType> {
    @Override
    public OperatorType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        switch (value) {
            case "=": return OperatorType.EQ;
            case "!=": return OperatorType.NE;
            case ">": return OperatorType.GT;
            case "<": return OperatorType.LT;
            case ">=": return OperatorType.GTE;
            case "<=": return OperatorType.LTE;
            default: throw new IllegalArgumentException("Unknown operator: " + value);
        }
    }
}
