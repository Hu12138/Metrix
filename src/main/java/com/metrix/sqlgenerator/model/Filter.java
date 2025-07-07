package com.metrix.sqlgenerator.model;

import lombok.Data;

@Data
public class Filter {
    private String field;
    private OperatorType operator;
    private String value;
}
