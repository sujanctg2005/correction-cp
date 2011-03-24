package com.edifecs.correction;

import com.edifecs.correction.utility.ModelCache;
import com.edifecs.correction.utility.ModelCacheEntry;
//import com.edifecs.correction.utility.UIFormatter;
import com.edifecs.correction.utility.UIDivFormatter;
import com.edifecs.correction.utility.CorrectionUIBean;

import com.edifecs.elm.ucf.domain.*;
import com.edifecs.elm.ucf.UCFException;
import com.edifecs.elm.ucf.UCFFactory;
import com.edifecs.elm.ucf.domain.UCFReader;

import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestModel {
	//private static ModelCache cache = new ModelCache();
	//private static int cacheID = 0;
	final Logger logger = LoggerFactory.getLogger(TestModel.class);
	private int myCacheID = 0;
	private int level = 0;
	private int nodeCnt = 0;
	private String content = "";
	private String errors = "";
	//private static final int pad = 10;
	//private static final int childPad = 10;
	private String contentTabLabel="";
	private static String errorTabLabel="Errors";
	//private UIFormatter uif;
	private UIDivFormatter uif;
	private CorrectionUIBean uiBean;
	private boolean editMode;
	public int divEndCnt=0;
	
	private Map<Integer, Property> propertyMap = new HashMap<Integer, Property>();
	private Map<Integer, Node> nodeMap = new HashMap<Integer, Node>();
	
	enum ViewMode { READ, EDIT}

	//private List<Property> uiProps = null;
	
	private URL url = null;
	String testUrl = "file:///C:/edifecs/RulesStudio23/samples/models/ECRHCF%20SAMPLE%20EXTRA.xml";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestModel tm = new TestModel();
		tm.test();
		//System.out.println("Node Count:"+nodeCnt);
	}
	
	public Map getNodeMap() {
		return nodeMap;
	}
	
	public Map getPropertyMap(){
		return propertyMap;
	}
	
	public String getContentTabLabel(){
		return contentTabLabel;
	}
	
	public Model test() {
		Model m = null;
		try {
			String inputFile="C:/edifecs/RulesStudio23/samples/models/ECRHCF SAMPLE EXTRA.xml";
			File input = new File(inputFile);
			//m =  getModel(input,"http://localhost:8080/postback/", "MARLINF");
		} catch (Exception e) {}
		return m;
	}
	
	public int getMyCacheID() {
		return myCacheID;
	}
	
	public CorrectionUIBean getCorrectionUIBean() {
		return uiBean;
	}
	
	//public Model getModel(InputStream input, String callbackUrl, String userInfo) throws Exception

	public Model getModel(String xmlUrl, String xmlId, String userInfo, String callbackUrl, String viewMode) throws Exception
	{
		//com.edifecs.domain.hcfd.support.OriginalServiceImpl imp = new com.edifecs.domain.hcfd.support.OriginalServiceImpl();	
		//imp.
		
		if(viewMode.equals(ViewMode.EDIT.toString()))
			editMode=true;
		else
			editMode=false;
		
		
		try {
			url = new URL(xmlUrl+xmlId);
		}catch (Exception e) { e.printStackTrace(); throw new Exception("Error creating url for: "+xmlUrl+xmlId); }
		
		UCFFactory factory = UCFFactory.getInstance();
		
		UCFReader <Model> reader = factory.getReader();
		
		//reader.read(InputStream);
		//reader.read(URL);
		//reader.read(Reader);
		//reader.read(File);
		Model pu = null;
		try {
			//String inputFile="C:/edifecs/RulesStudio23/samples/models/ECRHCF SAMPLE EXTRA.xml";
			//String errorFile="C:/TESTOUTPUT.XML";
			//ECRHCF SAMPLE EXTRA.xml
			try{
				pu = reader.read(url);
				logger.info(" Model type :" + pu.getClass());
				
				
			} catch(UCFException ue) {
				throw new Exception (ue.getMessage());
			}catch(Exception e) {
				throw(e);
			}
			
			
			
			System.out.println("read the file with no errors..");
			logger.info("read url:"+url);
			//log("<pre>model nodes at the top level: "+pu.getNodes().size());
			
			//uif = new UIFormatter();
			uif = new UIDivFormatter();
			uif.setEditMode(editMode); //true=edit, false=readonly
			//Get errors for the Errors Tab
			List <ErrorInfo> ei = pu.getErrorList(); //per Phillip.. this should NOT be deprecated.
			errors = uif.FormatErrors(ei);
			
			uif.setupFormInfo();
			level++;
			//uif.setOuterContentWrapper(); //start a table
			uif.FormatColHeading();  //set up the column headers for the table
			
			Collection <ChildInfo> childList = pu.getNodeChildList();
			
			//String node, int level, List <String> errorRef, List<ChildInfo> childList
			Viewable mv = (Viewable) pu;
			contentTabLabel = mv.getDisplayLabel();
			logger.info("Display label is: "+mv.getDisplayLabel());
			int nodeId = uif.formatNode(mv.getDisplayLabel(),level, pu.getErrorRefList(),childList);
			logger.info("Adding node to map:"+nodeId+" name:"+pu.getNodeName());
			nodeMap.put(nodeId, pu);
			
			Collection <Property> cp = pu.getProperties();
			uif.FormatProperty(cp,(Node)pu,level,nodeId, propertyMap);
			//log("Dumping properties...");
			int pcnt = 0;
			
			//recursive dump the rest of the Model.
			Collection <Node> nc = pu.getNodes();
			for(Node n : nc) {
				if(n instanceof com.edifecs.elm.ucf.domain.LifecycleUpdate)
					continue;
				
				nodeCnt++;
				//logger.debug("done getting root info, calling dump node..");
				//logger.info("LEVEL:"+level+" nodeID:"+nodeCnt);
				dumpNode(n, pu, level+1);
				divEndCnt++; //TODO: can remove later.. just here to keep track of the number of ending div tags..
				//uif.setCorrectUI("</div>");
				//logger.info("</div> (root winddown?) LEVEL:"+level+"  name:"+pu.getNodeName());
				String tmp = "";
				for(int x =0; x <level; x++) //TODO: can remove later.. just for debugging.
					tmp+=("==");
				
				uif.setCorrectUI("</div>");
				logger.info(tmp+"</DIV>"+n. getNodeName());
			}
			uif.setCorrectUI("</div>");
			logger.info("</DIV>"+pu.getNodeName());
			divEndCnt++;
			uif.setCorrectUI("");
			//end of processing Model tasks
			uif.setEndForm();
	//		uif.setEndOuterContentWrapper();
			logger.info("DIV STARTS:"+uif.divStartCnt);
			logger.info("DIV ENDS  :"+divEndCnt);
			content = uif.getCorrectUI();
			uiBean = new CorrectionUIBean();
			logger.info("XmlID:"+xmlId);
			uiBean.setXmlId(xmlId);
			logger.info("last id:"+uif.getCnt());
			uiBean.setIdCnt(uif.getCnt());
			uiBean.setEditMode(editMode);
			uiBean.setErrorTabLabel(errorTabLabel);
			uiBean.setErrorListData(errors);
			uiBean.setContentFormData(content);
			uiBean.setHeaderInfo("This is hard coded meta data (will come from model)!");
			uiBean.setContentTabLabel(contentTabLabel);
		} catch (UCFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw(e);
		} 
		return pu;
	}
	
	public void dumpNode(Node cn, Model pu, int level) {
		nodeCnt++;
		
		//logger.info("(dumpnode in) LEVEL:"+level+"  nodeID:"+nodeCnt+"  name:"+cn.getNodeName());
		Collection <ChildInfo> childList = cn.getNodeChildList();
		
		Viewable mv = (Viewable) cn;
		
		int nodeId = uif.formatNode(mv.getDisplayLabel(),level,cn.getErrorRefList(),childList);
		logger.info("Adding node to map:"+nodeId+" name:"+cn.getNodeName());
		nodeMap.put(nodeId, cn);	
		Collection <Property> cp = cn.getProperties();

		uif.FormatProperty(cp,cn,level, nodeId, propertyMap);
		
		//uif.setCorrectUI("</div "+uif.clearFloat);
		int pcnt = 0;
		Collection <Node> c = cn.getNodes();
		for(Node n : c) {
			//log(key);
			//uif.setCorrectUI("</div "+uif.clearFloat);
				dumpNode(n, pu, level+1);
				
				String tmp="";
				for(int x =0; x <level; x++){
					tmp+=("=="); }
				//logger.info("LEVEL (winddown?):"+level+"  nodeId:"+--nodeCnt+" name:"+n.getNodeName());
				uif.setCorrectUI("</DIV>");
				logger.info(tmp+"</DIV>"+cn.getNodeName());
				divEndCnt++; //just for dev .. counting to make sure they match start counts.
		}
	}
	
	
	//was used for testing.. not needed for the final version.
	public void postFragment(int cacheID) throws Exception {
		log("Posting xml...");
		Model m = null;
		try {
				m = ModelCache.getModel(cacheID).getModel();
		}catch(Exception e){
			throw new Exception("Model "+cacheID+" was not located.");
		}
		
		
		if(m == null) {
			throw new Exception("Model "+cacheID+" was not located.");
		}
		String callbackUrl=ModelCache.getModel(cacheID).getCallbackUrl();
		log("callbackURL:"+callbackUrl);
		UCFFactory factory = UCFFactory.getInstance();
		UCFWriter <Model> writer = factory.getWriter();
		File outFile = new File("c:/TESTOUT/XMLID-"+cacheID+".xml");
		outFile.getParentFile().mkdirs();
        FileOutputStream fos = null;
        try {
        	fos = new FileOutputStream(outFile);
            writer.write(fos,m);
        }catch(Exception fe){
        } finally {
        	try{
        		fos.close();
        	}catch(Exception e){}
        } 
		ModelCache.removeModel(cacheID);
	}
	
	public String getContent(){
		return content;
	}
	
	public String getErrors() {
		return errors;
	}
	
	// not used...
	public void log(String msg) {
		 try{
		 //System.out.println(msg);
			// dmp+=msg+"<br/>";
			 content+=msg;
		 }catch (NullPointerException npe) { System.out.println("NPE!!!"); }
	 }
}
