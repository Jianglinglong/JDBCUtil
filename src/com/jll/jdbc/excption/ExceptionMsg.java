package com.jll.jdbc.excption;

public enum ExceptionMsg {
    ERRO_PAGE("");

    private final String msg;

    public String getMsg() {
        return msg;
    }

    private ExceptionMsg(String msg) {
        this.msg = msg;
    }
}
