package com.edifecs.correction.utility;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edifecs.correction.utility.UIDivFormatter;
import com.edifecs.elm.ucf.domain.ChildInfo;
import com.edifecs.elm.ucf.domain.Model;
import com.edifecs.elm.ucf.domain.Node;
//import com.edifecs.domain
import com.edifecs.elm.ucf.domain.Viewable;
import com.edifecs.elm.ucf.domain.Property;

public class ModelNodeRetriever {
	
	final Logger logger = LoggerFactory.getLogger(ModelNodeRetriever.class);
	
	//1. Get the node stored in the nodeList...
	//2. Create a new node (specified by the UI selection - "nodeName")..
		//a. assign an ID to the new node and store the node in the nodeMap
	//3. Get the Node level info for the new node and then get the editable properties
		//b. assign an ID for each new property and put them in the propertyMap.
	//4. Get the list of children for the new node (will be used to create the "Add.." pulldown for the new node.
	//The Node row and the rows of editable props will need to be created and inserted into the UI.
	public String getNode(HttpSession session, String nodeName, String nodeId, String parentNodePadding) throws Exception {
		
		 //DateTimeFormatter formatter = ISODateTimeFormat.localTimeParser();
		// formatter
		Integer nodeID = Integer.parseInt(nodeId);
		logger.info("MNR: need to find the NODE with ID:"+nodeID);
	
		int basePadding = Integer.parseInt(parentNodePadding);
		int divPadding=basePadding+UIDivFormatter.nodePad;
	//	Model mdl = (Model) session.getAttribute("model");
		CorrectionUIBean uiBean = (CorrectionUIBean) session.getAttribute("uiBean");
		//get the last ID used in the UI.. (think of a pk for a database row).
		int cnt = uiBean.getIdCnt();
		
		//1. get node from nodeMap.. this it the parent node.. in the UI, it is the row that contains
		//the  "Add.." pull down list in which the user selected an entry.
		Map <Integer, Node> nodeMap     	= (Map) session.getAttribute("nodemap");
		Map <Integer, Property> propertyMap = (Map) session.getAttribute("propertymap");
		//Node n = nodeMap.get(nodeID);
		Node n = nodeMap.get(nodeID);
		if(n == null) logger.warn("node is null!!!!");
		logger.info("MNR: found the node for "+nodeID+" name:"+n.getNodeName());
		
		UIDivFormatter uif = new UIDivFormatter();
			
	// 2. create the new node on the parent node (selected from the "Add.." pulldown
	//	Collection <Node> nc = mdl.getNodes();	
		int c  =0;
		String div = "";
		String props = "";
		//Node newNode = n.getNode(nodeName);
		logger.info("Looking to add the node with name:"+nodeName);
		
		//uncomment and modify once Ron is done...
		Node nn = null;
		try{
			nn = n.createNode(nodeName);
			logger.info("parent class "+n.getClass());
			logger.info("child class "+nn.getClass());
			
			cnt++;
			nodeMap.put(new Integer(cnt),nn);
		}catch (IllegalStateException ise){
			throw new Exception("Maximum occurrences of "+nodeName+" has been reached.");
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("An error occurred while attempting to add: "+nodeName+".\nError:"+e.getMessage());
		}
		
	//3. get node level info needed to create the node level div...
		//childList will be used to build the "Add.." pulldown for the newly ceated Node...
		
		Collection <ChildInfo> childList = nn.getNodeChildList(); 
		
		Viewable mv = (Viewable) nn;
		div = uif.formatNodeDetail(mv.getDisplayLabel(), UIDivFormatter.nNameWidth-divPadding, divPadding, cnt, UIDivFormatter.delImage,"","","",childList);
		int count=0; //TODO: this will come from the UCF!!!.. should be the parent node it.
		int propPos = 0;
		Collection <Property> cp = nn.getProperties();
		logger.info("node props size:"+cp.size());
		logger.info("Node div:"+div);
		div+="<div id=\"DIV"+cnt+"\">";
		div+="<div id=\"DIVPROP-"+cnt+"\">";
		
		// get the properties for the new node, if they are editable and visible, put them in the propertyMap.
		// then pass them to UIDivFormatter.formatPropertyDetail.
		for(Property p : cp){	
			count++;
			propPos++;
		
			
			//String pname = p.getPropertyName(); 
			//logger.info("pname is:"+pname);
			String val = "";
			//try { val= p.getPropertyValue().toString(); } catch (NullPointerException npe) { val=""; }
		
			Viewable pv = (Viewable) p;
			if(pv.isEditable() && pv.isVisible()){
				//if(val!=""){
				String pname=pv.getDisplayLabel();
				cnt++;
				Map <String, ?> mp = p.getVersionMap();
				try{
				propertyMap.put(cnt, p); } catch(Exception e) { e.printStackTrace(); }
//public String formatPropertyDetail(int pad, String name, String val, Map<String, ?> m, String errorRef, String valueType, int nodeId, int propId, int propPos, String errorClass){
				div+=uif.formatPropertyDetail((divPadding+UIDivFormatter.childPad),pname,val, mp,"",p.getValueType().getTypeName(),nodeID, cnt, propPos,"");
				div+="</div>"+UIDivFormatter.clearFloat;
				//logger.info("Div is:"+div);
			//}
			}else { logger.info(pv.getDisplayLabel()+" is not visible or editable!!"); }
		} 
		div+="</div></div>"+UIDivFormatter.clearFloat;;
		logger.info("DIV:"+div);
		logger.info("Add node: id count:"+cnt);
		uiBean.setIdCnt(cnt);
	
		return div;	
	}
}
