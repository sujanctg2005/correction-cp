




package correction;
import com.edifecs.elm.ucf.domain.UCFWriter;
import org.codehaus.groovy.grails.web.json.JSONObject;
import com.sun.tools.internal.ws.processor.model.Request;

import com.edifecs.correction.TestModel;
import com.edifecs.correction.utility.FormInputItem;
import com.edifecs.elm.ucf.domain.Model
import com.edifecs.correction.utility.ModelNodeRetriever;
import com.edifecs.correction.utility.ModelSave;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import grails.converters.*

import org.json.*;
import org.json.simple.*;

class CorrectionController {
	//<input type=button onclick=\"javascript:window.opener='x';window.close();\" value=Close>
	//<input type=button onclick=\"self.close();\" value=Close>";
	final Logger logger = LoggerFactory.getLogger(CorrectionController.class);
	def debug = {}
	
	def addNode = {
		
		
		ModelNodeRetriever mnr = new ModelNodeRetriever();
		JSONObject jsonobj = new JSONObject();
		def node="";
		try{
			node = mnr.getNode(session, params.selected, params.parentnode, params.pad);
		} catch(Exception e) {
		 		e.printStackTrace();
				jsonobj.put("error", e.getMessage()); 
		 
		 }
		
		//jsonobj.put("nodedummy", "<div style=\"padding-left: "+(params.pad+UIDivFormatter.nodePad)+"px;\">Node->DIV"+params.divid+" node info goes here!</div>");
		jsonobj.put ("node", node);
		//logger.info(params.parentnode);
		//logger.info(params.selected);
		jsonobj.put ("divid",params.parentnode); //this is where to put the new node
		jsonobj.put("pad",params.pad); 
		
	
		/*
		PrintWriter out = response.getWriter();
		out.print(request.getParameter("jsonp_callback")+"(" + jsonobj.toString() + ")");
		out.flush();
		logger.info("ajax request:addNode");
		//render jsonobj as JSON; <- didnt work.
		//render(text : "TEST DATA FROM CONTROLLER", contentType:"text");
		*/
			
		System.out.println("class path1 : "+ getServletContext().getRealPath("/"));
		logger.info("ajax request:addNode");
		render(text:request.getParameter("jsonp_callback")+"(" + jsonobj.toString() + ")",contentType:"text/html",encoding:"UTF-8")
		
		}
	
	def renderui = {
		def closeButton = "<p/><input type=button onclick=\"window.open('','_self','');window.close();\" value=Close>";
		
		//InputStream xmlIn = params.fragment.getInputStream();
	
		//this gets the string contents of the file...Hang on to this for a while!!!
		//CommonsMultipartFile cmf = params.fragment;
		//print("getFileData:"+cmf.getFileItem().getString());
	
		//Get tokens from the request.
		def xmlId = params.xmlid;
		def xmlUrl = params.xmlurl;
		def callbackUrl = params.callbackurl;
		def userInfo = params.userinfo;
		def viewMode = params.viewmode;
		
		
		
		
		def auditInfo = "[AUDIT] User:"+userInfo+" Frag:"+xmlId+" Action:";
		logger.info(auditInfo+"Retrieve an Exception for Correction.");	
		TestModel tm = null;
		Model m = null;
		try {
			tm = new TestModel();
			//m = tm.getModel(xmlIn,callbackUrl,userInfo);
			logger.info("View mode:"+viewMode);
			m = tm.getModel(xmlUrl,xmlId,userInfo,callbackUrl,viewMode);
			session.putAt "uiBean", tm.getCorrectionUIBean();
			session.putAt "model", m;
			session.putAt "propertymap", tm.getPropertyMap();
			session.putAt "nodemap", tm.getNodeMap();
			logger.info(auditInfo+"Forwarding to UI");
			return m;
			//render (text: dmp, contentType:"text");
		} catch(Exception e) {
			e.printStackTrace();
			logger.error(auditInfo+"Error retrieving Exception frag. "+e.getMessage());
			render (text: "<html><body>Error retrieving Exception for editing: <br/>"+e.getMessage()+closeButton+"</body></html>", contentType:"text");
		}
	}
	//this would call the index.jsp
	def index = {
		def xml=params.xml;
		System.out.println("XML:"+xml);
		request.putAt "xml", xml;
		 def testData = "THIS IS A TEST";
		// render(text : testData, contentType:"text");
	}
	
	/** called via ajax to save the form data.
	 * 
	 */
	def postform = {
		def msg="Data was successfully saved.111";
		Map<Integer, FormInputItem> formMap = new HashMap<Integer, FormInputItem>();
		
		
		try {
			
			logger.info ("THE NEW POSTFORM was called!!!");
			Enumeration keys;
			String key;
			keys = request.getParameterNames();
			String myVal = "";
			
			/** 1. loop the form input
			 *  2. parse the input name to get attributes.
			 *  3. put attributes in a FormItemInput.
			 *  4. add propid, FormItemInput to the Map.
			 *  5. send the map and session to the backend.
			 *  6. get back the status of the save.
			 *  7. report the status (success or error) to the UI.
			 */
			while (keys.hasMoreElements())
			{		   
			   key = (String) keys.nextElement();
			   myVal = (String) request.getParameter(key);
			   //p:id:pos:parent:type
			   String [] inputNameInfo = key.split("-");
			   int inputNameInfoLength = inputNameInfo.length;
			   //logger.info("inputNameInfo:"+inputNameInfo.length);
			   if(inputNameInfoLength < 6){ //this means it may be part of the ajax framework, so ignore it!
				   logger.warn("input name [\""+key+"\"] has only "+inputNameInfoLength+" parsed items! (may be ok)");
			   		continue;	
			   }
			   FormInputItem fit = new FormInputItem();
			   try {
				   fit.setInputValue(myVal);
				   fit.setActionIndicator(inputNameInfo[0]); //TBD possibly U|D|I (update, delete, ignore. .whatever.
				   fit.setPropId(Integer.parseInt(inputNameInfo[1]));
				   fit.setUiPosition(Integer.parseInt(inputNameInfo[2]));
				   fit.setParentId(Integer.parseInt(inputNameInfo[3]));	   
				   fit.setUcfType(inputNameInfo[4]);
				   fit.setFormInputType(inputNameInfo[5]); //Property or Row...[P|R]
				   formMap.put(Integer.parseInt(inputNameInfo[1]),fit);
			   }catch (NumberFormatException e) {
			   	throw new Exception("Developer error - cannot set attributes of the input string, please check format and correct!");
			   }			   
			   logger.info(key +"="+myVal);
			}
			logger.info("Save-> map has:"+formMap.size()+" entries.");
			logger.info("ajax request:save form");
			
		}catch(Exception e){
			e.printStackTrace();
			msg="An error occurred while saving:\n"+e.getMessage();
		}
			ModelSave ms = new ModelSave();
			msg = ms.save(session, formMap);
			//create a simple JSON object and pass back a status message....
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("msg", msg);
			logger.info(jsonobj.toString());
			//out.print(request.getParameter("jsonp_callback")+"(" + jsonobj.toString() + ")");
			//out.print("success(" + jsonobj.toString() + ")");
			logger.info("callback:>"+request.getParameter("callback")+"<");
			/*PrintWriter out = response.getWriter();	
			out.print(request.getParameter("callback")+"("+ jsonobj.toString() +")");
			out.flush();*/
			
			render(text:request.getParameter("callback")+"("+ jsonobj.toString() +")",contentType:"text/html",encoding:"UTF-8")
			
	}
	def downloadFile= {
		
		ModelSave ms = new ModelSave();
		UCFWriter<Model> writer =ms.getModelWriter();		
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition" ,"attachment; filename=text.xml");	
		writer.write(response.getOutputStream(), (Model)session.getAttribute("model"));
		response.getOutputStream().flush();
		}
	def keepAlive = {
		JSONObject jsonobj = new JSONObject();
		logger.info("ajax request to keep session alive..");
		render(text:request.getParameter("jsonp_callback")+"(" + jsonobj.toString() + ")",contentType:"text/html",encoding:"UTF-8")
	}
}
