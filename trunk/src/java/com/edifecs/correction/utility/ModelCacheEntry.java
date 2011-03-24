package com.edifecs.correction.utility;

import com.edifecs.elm.ucf.domain.Model;

public class ModelCacheEntry {
	private Model model;
	private String callbackUrl;
	private String userInfo;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void setModel(Model model) {
		this.model=model;
	}
	
	public Model getModel() {
		return model;
	}
	
	public void setCallbackuUrl(String callbackUrl) {
		this.callbackUrl=callbackUrl;
	}
	
	public String getCallbackUrl() {
		return callbackUrl;
	}
	
	public void setUserInfo(String userInfo) {
		this.userInfo=userInfo;
	}
	
	public String getUserInfo() {
		return userInfo;
	}
}
