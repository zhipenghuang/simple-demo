package com.hzp.test.exception;

/**
 * @author yunmu
 * @create 2018/9/19.
 */
public class BaseException extends RuntimeException implements ExceptionScalable {

	private Errors error;
	private Object data;

	public BaseException() {
	}

	public BaseException(Errors error) {
		super(error.getMessage());
		this.error = error;
	}

	public BaseException(Errors error, String msg) {
		super(msg);
		this.error = error;
		this.error.setMessage(msg);
	}

	public BaseException(Errors error, String msg, Object data) {
		super(msg);
		this.error = error;
		this.error.setMessage(msg);
		this.data = data;
	}

	@Override
    public Errors getError() {
		return error;
	}

	@Override
    public Object getData() {
		return data;
	}

}
