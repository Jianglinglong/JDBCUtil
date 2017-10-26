package com.jll.jdbc.consts;

public enum SQLOperator {
    SELECT(" SELECT "), INSERT(" INSERT INTO "), UPDATE(" UPDATE "),SET(" SET "),
    DELETE("DELETE FROM "),FROM(" FROM "), WHERE(" WHERE "),VALUES(" VALUES "),
    LIKE(" LIKE "),AND(" AND "),LIMIT(" LIMIT "),
    LIKE_PARAM_AND(" LIKE ? AND "),LIKE_PARAM("%"),
    EQUAL_PARAM_AND(" = ? and "),NOT_EQUAL_PARAM_AND(" <> ? and "),
    EQUAL_PARAM(" = ? "),PARAM(" ? "),COMMA (","),LEFT_BRACKET("("),RIGHT_BRACKET(")"),
    BLANK(" ");

    private SQLOperator(String operator) {
        this.operator = operator;
    }

    private final String operator;

    public String getOperator() {
        return operator;
    }
}
