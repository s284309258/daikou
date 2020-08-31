package com.cff.springbootwork.dto;

/**
 */
public class ResultModel {

	private String errorCode;
	private String message;
	private Object remark;
	private Object data;

	public ResultModel(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public ResultModel() {
	}

	public ResultModel(String errorCode, String message, Object data) {
		this.errorCode = errorCode;
		this.message = message;
		this.data = data;
	}

	public ResultModel(ResultCode resultCodeEnum, Object data) {
		this.errorCode = resultCodeEnum.getCode();
		this.message = resultCodeEnum.getDesc();
		this.data = data;
	}

	public ResultModel(ResultCode resultCodeEnum, Object data, Object remark) {
		this.errorCode = resultCodeEnum.getCode();
		this.message = resultCodeEnum.getDesc();
		this.data = data;
		this.remark = remark;
	}

	public ResultModel(ResultCode resultCodeEnum) {
		this.errorCode = resultCodeEnum.getCode();
		this.message = resultCodeEnum.getDesc();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static ResultModel ok() {
		return new ResultModel(ResultCode.CODE_00000);
	}

	public static ResultModel ok(Object data) {
		return new ResultModel(ResultCode.CODE_00000, data);
	}

	public static ResultModel unAuth() {
		return new ResultModel(ResultCode.CODE_00002);
	}

	public static ResultModel unAuth(Object data) {
		return new ResultModel(ResultCode.CODE_00002, data);
	}

	public static ResultModel error(String message) {
		return new ResultModel(ResultCode.CODE_00001.getCode(), message);
	}

	public Object getRemark() {
		return remark;
	}

	public void setRemark(Object remark) {
		this.remark = remark;
	}

}
