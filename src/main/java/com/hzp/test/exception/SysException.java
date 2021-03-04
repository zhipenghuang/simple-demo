package com.hzp.test.exception;

/**
 * @author yunmu
 * @create 2018/9/19.
 */
public class SysException extends BaseException {

	public SysException() {
		super();
	}

	public SysException(Errors error, String msg) {
		super(error, msg);
	}

	public SysException(Errors error) {
		super(error);
	}
}
