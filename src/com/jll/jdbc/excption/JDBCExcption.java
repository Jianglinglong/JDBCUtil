package com.jll.jdbc.excption;

public class JDBCExcption extends RuntimeException {

	private static final long serialVersionUID = -1554764216533964580L;

	public JDBCExcption() {
		super();
	}

	public JDBCExcption(String message) {
		super(message);
	}
}
