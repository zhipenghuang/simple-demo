package com.hzp.test.exception;


/**
 * @author yunmu
 * @create 2018/9/19.
 */
public interface ExceptionScalable {

	/**
	 * 错误对象
	 * @return
	 */
	Errors getError();

	/**
	 * 引起执行异常的数据, 如输入数据，或执行语句等
	 * @return
	 */
	Object getData();
}
