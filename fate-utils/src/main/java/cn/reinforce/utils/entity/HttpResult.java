package cn.reinforce.utils.entity;

public class HttpResult {

	private int statusCode;
	
	private String result;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "HttpResult [statusCode=" + statusCode + ", result=" + result + "]";
	}
	
}
