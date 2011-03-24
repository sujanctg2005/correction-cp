package com.edifecs.correction.utility;

public class FormInputItem {
	private int propId;
	private String inputValue;
	private String actionIndicator;
	private String ucfType;
	private int parentId;
	private int uiPosition;
	private String formInputType;
	
	public String getFormInputType() {
		return formInputType;
	}
	public void setFormInputType(String formInputType) {
		this.formInputType = formInputType;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getUiPosition() {
		return uiPosition;
	}
	public void setUiPosition(int uiPosition) {
		this.uiPosition = uiPosition;
	}
	public int getPropId() {
		return propId;
	}
	public void setPropId(int propId) {
		this.propId = propId;
	}
	public String getInputValue() {
		return inputValue;
	}
	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}
	public String getActionIndicator() {
		return actionIndicator;
	}
	public void setActionIndicator(String actionIndicator) {
		this.actionIndicator = actionIndicator;
	}
	public String getUcfType() {
		return ucfType;
	}
	public void setUcfType(String ucfType) {
		this.ucfType = ucfType;
	}
}
