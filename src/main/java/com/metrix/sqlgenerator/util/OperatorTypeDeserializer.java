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
        switch (value.toUpperCase()) {
            case "=":
            case "EQ": return OperatorType.EQ;
            case "!=":
            case "NE": return OperatorType.NE;
            case ">":
            case "GT": return OperatorType.GT;
            case "<":
            case "LT": return OperatorType.LT;
            case ">=":
            case "GTE": return OperatorType.GTE;
            case "<=":
            case "LTE": return OperatorType.LTE;
            case "BETWEEN": return OperatorType.BETWEEN;
            default: throw new IllegalArgumentException("Unknown operator: " + value);
        }
    }
}
