package com.edifecs.correction.utility;

import java.io.Serializable;

public class CorrectionUIBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String headerInfo;
	private String errorListData;
	private String contentFormData;
	private String contentTabLabel;
	private String errorTabLabel;
	private boolean editMode;
	private String xmlId;
	
	public String getXmlId() {
		return xmlId;
	}
	public void setXmlId(String xmlId) {
		this.xmlId = xmlId;
	}
	public int getIdCnt() {
		return idCnt;
	}
	public void setIdCnt(int idCnt) {
		this.idCnt = idCnt;
	}
	private int idCnt;

	public boolean isEditMode() {
		return editMode;
	}
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
	public String getHeaderInfo() {
		return headerInfo;
	}
	public void setHeaderInfo(String headerInfo) {
		this.headerInfo = headerInfo;
	}
	public String getErrorListData() {
		return errorListData;
	}
	public void setErrorListData(String errorListData) {
		this.errorListData = errorListData;
	}
	public String getContentFormData() {
		return contentFormData;
	}
	public void setContentFormData(String contentFormData) {
		this.contentFormData = contentFormData;
	}
	public String getContentTabLabel() {
		return contentTabLabel;
	}
	public void setContentTabLabel(String contentTabLabel) {
		this.contentTabLabel = contentTabLabel;
	}
	public String getErrorTabLabel() {
		return errorTabLabel;
	}
	public void setErrorTabLabel(String errorTabLabel) {
		this.errorTabLabel = errorTabLabel;
	}

}
