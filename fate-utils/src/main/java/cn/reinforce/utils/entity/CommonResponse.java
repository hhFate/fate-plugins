package cn.reinforce.utils.entity;

import com.google.gson.annotations.Expose;

public class CommonResponse {

	@Expose
	public boolean success;
	
	@Expose
	public int error_code;
	
	@Expose
	public String msg;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
