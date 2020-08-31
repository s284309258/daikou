package com.renegade.exception;

/**
 * ErrorController可对全局错误进行处理，但是其获取不到异常的具体信息，同时也无法根据异常类型进行不同的响应，例如对自定义异常的处理
 * 而@ControllerAdvice可对全局异常进行捕获，包括自定义异常
 * 需要清楚的是，其是应用于对的控制器抛出的异常进行处理，而对于404这样不会进入控制器处理的异常不起作用，所以此时还是要依靠ErrorController来处理
 * <p>
 * Title: BDException
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * @author NIC丶四叶草^Renegade
 * 
 * @date 2019年5月31日
 */
public class BDException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String msg;
	private int code = 500;

	public BDException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public BDException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public BDException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public BDException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
