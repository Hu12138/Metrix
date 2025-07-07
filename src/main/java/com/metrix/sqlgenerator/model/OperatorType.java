package com.metrix.sqlgenerator.model;

public enum OperatorType {
    EQ("="), 
    NE("!="), 
    GT(">"), 
    LT("<"), 
    GTE(">="), 
    LTE("<=");

    private final String symbol;

    OperatorType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
