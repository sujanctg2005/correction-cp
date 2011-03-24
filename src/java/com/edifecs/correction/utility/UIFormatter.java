package com.edifecs.correction.utility;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.edifecs.elm.ucf.domain.Model;
import com.edifecs.elm.ucf.domain.ErrorInfo;
import com.edifecs.elm.ucf.domain.Node;
import com.edifecs.elm.ucf.domain.Property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIFormatter {
	final Logger logger = LoggerFactory.getLogger(UIFormatter.class);
	private String correctUI = "";
	private String errorUI = "";
	private String contentTabLabel="";
	private String errorTabLabel="";
	private String contentColHeading="";
	private String formInfo = "";
	private static final int nodePad = 15;
	private static final int childPad = 15;
	private static String addImage="<img onmouseover=\"style.cursor='pointer'\" onclick=\"ajaxCall('addCriteria','add');\" height=\"20px\" width=\"20px\" src=\"/Correction/images/correction/list_add.png\"></img>";
	private static String delImage="<img onmouseover=\"style.cursor='pointer'\" onclick=\"ajaxCall('removeCriteria','rem');\" height=\"15px\" width=\"15px\" src=\"/Correction/images/correction/erase.png\"></img>";
	private boolean editMode;
	
	public boolean isEditMode() {
		return editMode;
	}


	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}


	public void setOuterContentWrapper() {
		setCorrectUI("<table class=\"contentTable\" border=\"0\">");
	}
	
	
	public void setupFormInfo()
	{
		formInfo="<Form action=\"/Correction/correction/postform\" method=\"POST\">";
		setCorrectUI(formInfo);
	}

	public void FormatColHeading() {
		contentColHeading="<tr><th class=\"nameColHeader\">Name</th><th></th>"+
			"<th class=\"currentColHeader\">Current</th><th></th>"+
			"<th class=\"originalColHeader\">Original</th></tr>";
		setCorrectUI(contentColHeading);
	}
	
	public void FormatNode(String node, int level, String errorRef){
		int pad = 0;
		String tmpAddImg="";
		String tmpDelImg="";
		String errorClass="";
		//dont allow add/delete at the root level
		if(errorRef!=null && !errorRef.equals("null")){
			errorClass="error";
		} else {
			errorRef="";
		}
		if(level > 1) {
			tmpAddImg = addImage;
			tmpDelImg = delImage;
			pad = level*nodePad;
		} else {
			tmpAddImg = "";
			tmpDelImg = "";
			
			//TODO: remove hard code..
			errorRef="<a name=\"error-1\"></a>";
		}
		//TODO: remove hard code.
		//errorClass="error";
		setCorrectUI("<tr class=\"nodetr"+errorClass+"\">"+errorRef+"<td style=\"padding-left:"+pad+"px;\""+
				" class=\"nodeNameCol\">"+node+"</td>"+
				"<td class=\"nodeDelete\">"+tmpDelImg+"</td><td class=\"nodeList\"> </td>"+
				"<td class=\"nodeAdd\">"+tmpAddImg+"</td><td class=\"nodeOriginal\"></tr>");
	}
	
	//this is called with the model.. from the root level of the model
	public void FormatProperty(Collection<Property> cp, Model pu, int level){
		int pad=(level*nodePad)+childPad;
		
		for(Property p : cp){
			//logger.info("prop name:"+p.getPropertyName());
			String pname = pu.getProperty(p.getPropertyName()).toString();
			if(pname.indexOf(" = ") > -1){
				String val=""; // so dont see "null" if it is null.
				if(p.getPropertyValue() != null) { val=p.getPropertyValue().toString(); } 
				
				Map <String,?> m = p.getVersionMap();
				formatPropertyDetail(pad,p.getPropertyName(),val, m, p.getErrorRef(),p.getValueType().getTypeName());
			}
		}
	}
	
	//this is called by dump node .... for all child nodes of the root node.
	public void FormatProperty(Collection<Property> cp, Node pu, int level){
		int pad=(level*nodePad)+childPad;

		for(Property p : cp){
			//logger.info("prop name:"+p.getPropertyName());
			String pname = pu.getProperty(p.getPropertyName()).toString();
			if(pname.indexOf(" = ") > -1){
				String val=""; // so dont see "null" if it is null.
				if(p.getPropertyValue() != null) { val=p.getPropertyValue().toString(); }
				
				Map <String,?> m = p.getVersionMap();
				formatPropertyDetail(pad,p.getPropertyName(),val, m,p.getErrorRef(),p.getValueType().getTypeName());
			}
		}
	}

	//format the property row, find and format the original.
	private void formatPropertyDetail(int pad, String name, String val, Map<String, ?> m, String errorRef, String valueType){
		String errorClass="";
		if(errorRef!=null && !errorRef.equals("null")){
			errorClass="error";
		} else {
			errorClass="";
		}
		//TODO: remove hard coding for testing.. check for label, check if should be editable.
		//errorClass="error";
		setCorrectUI("<tr><td style=\"padding-left:"+pad+"px;\" class=\"namecol\">"+name+"</td>"+
				"<td class=\"delete\">"+delImage+"</td>"+
				"<td class=\"current\">"+
				"<input onFocus=\"setOriginalValue(this.value);\" "+
					"onBlur=\"setEditedValue(this.value,'"+valueType+"',this);\" "+
					"class=\"formInputField"+errorClass+"\" type=\"text\" value=\""+val+
				"\"></td><td class=\"add\"></td>");
		
		Object original="";
		for(String key : m.keySet()) {
			if(key.equalsIgnoreCase("original")){
				original=(Object)m.get(key);
				if(original.equals(null) || original.equals("null")){
					original = "";
				}
			}
		}
		setCorrectUI("<td class=\"original\">"+original+
				"</td></tr>");

	}
	
	private String FormatErrorHeader() {
		return "<tr class=\"errorTable\">"+
				"<th class=\"errorTable\">Tracking ID</th>"+
				"<th class=\"errorTable\">Error ID</th>"+
				"<th class=\"errorTable\" width=\"300\">Description</th>"+
				"<th class=\"errorTable\">Category</th>"+
				"<th class=\"errorTable\">Severity</th></tr>";
	}
	
	public String FormatErrors(List<ErrorInfo> errors) {
		errorUI+="<table class=\"errorTable\">";
		errorUI+=FormatErrorHeader();
		
		int eCnt = 0;
		for(ErrorInfo e : errors) {
			//temp counter until get errors:
			//TODO:get the real error id
			eCnt++;
			errorUI+="<td class=\"errorTable\"></td>"; //trk id
			errorUI+="<td class=\"errorTable\"><a href=\"javascript:void(null)\" onclick=\"javascript:navigateError('error-"+eCnt+"');\">"+e.getErrorID()+"-"+eCnt+"</td>";
			//errorUI+="<td>ErorSequence:</td><td>"+e.getErrorSequence()+"</td>";
			errorUI+="<td class=\"errorTable\">"+e.getMessage()+"</td>";
			errorUI+="<td class=\"errorTable\"></td>"; //Category
			errorUI+="<td class=\"errorTable\">"+e.getSeverity()+"</td></tr>";
		}
		errorUI+="</table>";
		return errorUI;
	}

	//getters - setters
	public String getCorrectUI() {
		return correctUI;
	}
	public void setCorrectUI(String correctUI) {
		this.correctUI += correctUI;
	}
	
	public String getContentColHeading() {
		return contentColHeading;
	}

	public void setContentColHeading(String contentColHeading) {
		this.contentColHeading = contentColHeading;
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

	public void setEndForm() {
		setCorrectUI("</form>");
	}

	public void setEndOuterContentWrapper() {
		//TODO: remove hard code..
		setCorrectUI("</a><tr><td colspan=5></td></tr>");
		setCorrectUI("</table>");
		setCorrectUI("<a name=\"error-2\"></a><p><p><p><a name=\"error-3\">");
	}

}
