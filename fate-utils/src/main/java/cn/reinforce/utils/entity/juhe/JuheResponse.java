package cn.reinforce.utils.entity.juhe;

import java.util.Map;

import com.google.gson.annotations.Expose;

public class JuheResponse {

	@Expose
	private int resultcode;
	
	@Expose
	private String reason;
	
	@Expose
	private String error_code;
	
	@Expose
	private Map<String, Object> result;

	public int getResultcode() {
		return resultcode;
	}

	public void setResultcode(int resultcode) {
		this.resultcode = resultcode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	
}
