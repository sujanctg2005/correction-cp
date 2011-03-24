package com.edifecs.correction.utility;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edifecs.elm.ucf.domain.ChildInfo;
import com.edifecs.elm.ucf.domain.ErrorInfo;
import com.edifecs.elm.ucf.domain.Model;
import com.edifecs.elm.ucf.domain.Viewable;
import com.edifecs.elm.ucf.domain.Node;
import com.edifecs.elm.ucf.domain.Property;

public class UIDivFormatter {
	final Logger logger = LoggerFactory.getLogger(UIDivFormatter.class);
	
	//also need when add node or add property called, so exposing them.
	//may be good candidates for a props file.
	public static final int nodePad    = 10;
	public static final int childPad   = 15;
	public static final int pNameWidth = 310;
	public static final int nNameWidth = 304;
	public static String delImage = "<img height=\"15px\" width=\"15px\" src=\"/Correction/images/correction/erase.png\"></img>";
	
	private String correctUI = "";
	private String errorUI = "";
	private String contentTabLabel = "";
	private String errorTabLabel = "";
	private String contentColHeading = "";
	private String formInfo = "";
	private Map <String, String> errorMap;
	
	
	//TODO: note addImage is not currently used, but this may be need later, remove if not needed.
	private static String addImage = "<img onmouseover=\"style.cursor='pointer'\" onclick=\"ajaxCall('addCriteria','add');\" height=\"20px\" width=\"20px\" src=\"/Correction/images/correction/list_add.png\"></img>";
	
	private boolean editMode;
	public static String clearFloat = "<span class=\"clearFloat\"></span>";
	private int cnt = 0;
	
	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	private int propCnt = 0;
	public int divStartCnt=0;
	
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
		formInfo="<Form id=\"correction\" name=\"correction\" action=\"/Correction/correction/postform\" method=\"POST\">";
		setCorrectUI(formInfo);
	}

	//"<div class=\"showall\" onmouseover=\"style.cursor='pointer'\" onclick=\"showAllDivs();\">+</div>"+
	//"<div class=\"hideAll\" onmouseover=\"style.cursor='pointer'\" onclick=\"hideAllDivs();\">-</div>"+
	public void FormatColHeading() {
		contentColHeading="<div style=\"float: left;\" class=\"contentHeader\">"+
				"<div class=\"contentheadername\">Name</div>"+
				"<div class=\"contentheadercurrent\">Current</div>"+
			"<div class=\"contentheaderoriginal\">Original</div></div>"+clearFloat;
		setCorrectUI(contentColHeading);
	}
	
	public int formatNode(String node, int level, List <String> errorRef, Collection<ChildInfo> childList){
		//TODO: need to get a list of available nodes/properties for the pull down list
		cnt++; //increment the object counter (nodes or properties)
		int pad = 0;
		String tmpAddImg="";
		String tmpDelImg="";
		String errorClass="";
		String errorAnchors="";
		String errorMessages="";
		String errorPopupCmd="";
		//logger.info("number of errors:"+errorRef.size());
		if(errorRef != null){
			errorClass="nodeError";
			logger.info("number of errors:"+errorRef.size());
			errorMessages="Error(s) in "+node+":";
			for(String ei: errorRef){
				logger.info("error:"+ei);
				errorMessages+="<br/>"+getErrorMessage(ei);
				errorAnchors+="<a name=\""+ei+"\"></a>";
				//logger.info("error id: "+ei.getErrorID());
				//logger.info("error msg:"+ei.getMessage());
			}
		
			errorPopupCmd=" onMouseOver=\"overlib('"+errorMessages+"',BORDER, 3);\" onMouseout=\"nd();\"";
		} else {
			errorClass="";
		}
		logger.info("Errors:"+errorMessages);
		//dont allow add/delete at the root level
	//	if(errorRef.size() > 0){
		//	errorClass="error";
		//} else {
		//	errorClass="";
	//	}
		if(level > 1) {
			tmpAddImg = addImage;
			tmpDelImg = delImage;
			pad = level*nodePad;
		} else {
			tmpAddImg = "&nbsp;";
			tmpDelImg = "&nbsp;";
			pad = (level*nodePad)+3;
			//TODO: remove hard code..
			//errorRef="<a name=\"error-1\"></a>";
		}
		//TODO: remove hard code.
		//errorClass="error";
		divStartCnt++;
		String tmp="";
		for(int x =0; x <level; x++)
			tmp+=("==");
		logger.info(tmp+"<DIV> "+node+"  "+level);
		setCorrectUI(formatNodeDetail(node,nNameWidth-pad,pad,cnt,tmpDelImg,errorClass,errorAnchors,errorPopupCmd, childList));			 
		setCorrectUI("<div id=\"DIV"+cnt+"\" class=\"modelnode\">"); //start the next child div.. is this correct??
		return cnt;
	}
	
	public String formatNodeDetail(String node, int width, int pad, int cnt, String delImage, String errorClass, String errorAnchors, String errorPopupCmd, Collection<ChildInfo> childList) {
		/* String cursor="";
		if(errorClass!="")
			cursor="\"cursor:pointer\";"; */
	
		return "<div class=\"nodecolor\">"+ //div for a node row.
			"<div id=\"vis-"+cnt+"-0\" onmouseover=\"style.cursor='pointer'\""+
			"onclick=\"toggleNodeVisibility(this,this.id)\" class=\"modelnodeheademinusplus\">-</div>"+
			"<div id=\"NODELBL-"+cnt+"\" style=\"width:"+width+"px; padding-left:"+pad+"px; \" "+
			"class=\"modelnodeheadername "+errorClass+"\" "+errorPopupCmd+">"+node+errorAnchors+"</div>"+
			"<div id=\"del-"+cnt+"\" onmouseover=\"style.cursor='pointer';\" onclick=\"toggleDeleteRevertNode(this.id,this);\" "+
			"class=\"modelpropertydelete\">"+delImage+"<input type=\"hidden\" id=\"HID-"+cnt+"\"></div>" +
			"<div class=\"modelnodeheadernodelist\">&nbsp;"+getAvailablePropsNodes(cnt, childList)+"</div>"+
			"<div class=\"modelnodeheaderadd\">&nbsp;</div>"+
			"</div>"+clearFloat;
	}

	
	//function addNodeRequest(input,callback,nodeid){ <- what js is doing....
	//"\"style=\"background-color:green; color:white; font-weight:bold;\" width=\"25\"
	private String getAvailablePropsNodes(int nodeId, Collection<ChildInfo> childList){
	//	for(ChildInfo ci:childList){	
	//		logger.info("child name:"+ci.getName()+" type:"+ ci.getType().getName()+" label"+ ci.getLabel());
	///	} 	

		String available="<select class=\"addNodeSelect\" name=\"addlistitem\" id=\"SEL-"+nodeId+"\" onchange=\"addNodeRequest(this.options[this.selectedIndex].value,this.id);\">";
		available+="<option value=\"\"> Add...</option>";
		int availableCnt=0;
		for(ChildInfo ci: childList) {
			//TODO: pick up Ron's change: isNode() - Boolean
			//logger.info("is this child a node:"+ci.isNode());
			
			if(ci.getType().toString().indexOf("Property") == -1) {
			//if(ci.isNode()){
				availableCnt++;
				available+="<option value=\""+ci.getName()+"\">"+ci.getLabel()+"</option>";
			}
		}
					/*	"<option value=\"\"> Add...</option>"+
						"<option value=\"N1\">Node1</option>"+
						"<option value=\"N2\">Node2</option>"+
						"<option value=\"N3\">Node3</option>"+
						"<option value=\"N4\">Node4</option>"+
						"<option value=\"P1\">Property1</option>"+
						"<option value=\"P1\">Property2</option>"+
						*/
		available+="</select>";
		if(availableCnt == 0){
			available = "&nbsp;";
		}
		return available;
	}
	
	//this is called by dump node .... for all child nodes of the root node.
	public void FormatProperty(Collection<Property> cp, Node pu, int level, int nodeId, Map<Integer, Property> propertyMap){
		//logger.info(">>>>>>>>> formatProperty ..");
		int pad=(level*nodePad)+childPad;
		int nodeID=nodeId;
		cnt++;
		int propPos=1;
		//Viewable v = (Viewable) pu; //
		//
		setCorrectUI("<div id=\"DIVPROP-"+nodeId+"\">");
		
		for(Property p : cp){
			logger.info("prop name sujan: "+p.getPropertyName() + " main node : "+ pu.getNodeName());
			String pname = pu.getProperty(p.getPropertyName()).toString();
			//TODO: this should be done by finding the property attribute when UCF ready!!
			Viewable prop = (Viewable) p;
			if(prop.isVisible() && prop.isEditable()){
				String val=""; // so dont see "null" if it is null.
				if(p.getPropertyValue() != null) { val=p.getPropertyValue().toString(); }
				propertyMap.put(cnt, p);
				Map <String,?> m = p.getVersionMap();
				formatPropertyDetailInternal(pad,prop.getDisplayLabel(),val, m,p.getErrorRef(),p.getValueType().getTypeName(),nodeID, cnt, propPos);
				cnt++;
				propPos++;
			}
		}
		setCorrectUI("</div>");
	}

	
	//format the property row, find and format the original version.
	private void formatPropertyDetailInternal(int pad, String name, String val, Map<String, ?> m, String errorRef, String valueType, int nodeId, int propId, int propPos){
		String errorClass="";
		if(errorRef!=null && !errorRef.equals("null")){
			errorClass="error";
		} else {
			errorClass="";
		}
		setCorrectUI(formatPropertyDetail(pad, name,  val, m, errorRef, valueType, nodeId, propId, propPos, errorClass));
		setCorrectUI("</div></div>"+clearFloat);
	}
	
	//So add node can use this formatting as well
	//int pad, String name, String val, Map<String, ?> m, String errorRef, String valueType, int nodeId, int propId, int propPos
	
	public String formatPropertyDetail(int pad, String name, String val, Map<String, ?> m, String errorRef, String valueType, int nodeId, int propId, int propPos, String errorClass){	
	String detail = "";
	detail = "<div class=\"propertyrow propertycolor\">"+
				 "<div style=\"padding-left:"+pad+"px; width:"+(pNameWidth-pad)+"px;\" class=\"propertyrowname\">"+name+"</div>"+
				 "<div class=\"propertyrowdelete\">&nbsp;</div>"+
				 "<div class=\"propertyrowcurrent\">"+
				 	"<input name=\"U-"+propId+"-"+propPos+"-"+nodeId+"-"+valueType+"-P\" "+
				 		"onFocus=\"setOriginalValue(this.value);\" "+
				 		"onBlur=\"setEditedValue(this.value,'"+valueType+"',this);\" " +
				 		"class=\"formInputField"+errorClass+"\" type=\"text\" value=\""+val+"\"/></div>"+
				 "<div class=\"propertyrowadd\">&nbsp;</div>"+
				 "<div class=\"propertyroworiginal\">";
	//style=\"width:24px;\"
	
		Object original="";
		for(String key : m.keySet()) {
			if(key.equalsIgnoreCase("original")){
				original=(Object)m.get(key);
				if(original.equals(null) || original.equals("null")){
					original = "";
				}
			}
		}
		detail+=original;
		return detail;
	}
	
	private String FormatErrorHeader() {
		return "<tr class=\"errorTable\">"+
				"<th class=\"errorTable\">Tracking ID</th>"+
				"<th class=\"errorTable\">Error ID</th>"+
				"<th class=\"errorTable\" width=\"300\">Description</th>"+
				"<th class=\"errorTable\">Category</th>"+
				"<th class=\"errorTable\">Severity</th></tr>";
	}
	
	private void setupErrorLookup(List <ErrorInfo> errors){
		errorMap = new HashMap <String, String>(); 
		for(ErrorInfo ei: errors){
			errorMap.put(ei.getErrorRefID(), ei.getMessage());
		}
	}
	
	public String getErrorMessage(String errorID)
	{
		if(errorMap != null) {
			return errorMap.get(errorID);
		} else {
			return "";
		}
	}
	
	public String FormatErrors(List<ErrorInfo> errors) {
		setupErrorLookup(errors);
		errorUI+="<table class=\"errorTable\">";
		errorUI+=FormatErrorHeader();
		
		int eCnt = 0;
		for(ErrorInfo e : errors) {
			//temp counter until get errors:
			//TODO:get the real error id
			eCnt++;
			
			errorUI+="<td class=\"errorTable\"></td>"; //trk id
			errorUI+="<td class=\"errorTable\"><a href=\"javascript:void(null)\" onclick=\"javascript:navigateError('"+e.getErrorRefID()+"');\">"+e.getErrorRefID()+"</td>";
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
