package com.lumiata.exception;

@SuppressWarnings("serial")
public class ApiException extends Exception{
	@SuppressWarnings("unused")
	private int code;

	public ApiException(int code, String msg) {
		super(msg);
		this.code = code;
	}
}
