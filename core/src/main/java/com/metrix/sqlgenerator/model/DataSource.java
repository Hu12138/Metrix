package com.metrix.sqlgenerator.model;

import lombok.Data;

@Data
public class DataSource {
    private String database;
    private String table;
    private String dateField;
}
