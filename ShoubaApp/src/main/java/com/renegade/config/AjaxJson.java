package com.renegade.config;

public class AjaxJson
{
	private String msg;
	private boolean success;
	private Object data;
	private String token;
	private String code;

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "AjaxJson [msg=" + msg + ", success=" + success + ", data=" + data + ", token=" + token + ", code="
				+ code + "]";
	}


}
