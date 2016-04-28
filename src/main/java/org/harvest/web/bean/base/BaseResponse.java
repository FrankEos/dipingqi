package org.harvest.web.bean.base;

public class BaseResponse {

	private int resultCode;

	private String resultDesc;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	@Override
	public String toString() {
		return "BaseResultResponse [resultCode=" + resultCode + ", resultDesc=" + resultDesc + "]";
	}

}
