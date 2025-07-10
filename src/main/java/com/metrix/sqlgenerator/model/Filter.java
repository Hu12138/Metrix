package com.metrix.sqlgenerator.model;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
public class Filter {
    private String field;
    private OperatorType operator;
    private Object value;
    private List<String> values = new ArrayList<>();
}
