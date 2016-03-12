package com.xnx3.exception;

/**
 * 方法没有走正常流程，某个环节出问题了，无返回值，特抛出异常，以便设置默认值
 * @author 管雷鸣
 *
 */
public class NotReturnValueException extends Exception {
	
	public NotReturnValueException() {
		// TODO Auto-generated constructor stub
	}
	
	public NotReturnValueException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}
}
