package com.edifecs.correction.utility;

import com.edifecs.elm.ucf.domain.*;
import com.edifecs.elm.ucf.UCFException;
import com.edifecs.elm.ucf.UCFFactory;
import com.edifecs.elm.ucf.domain.UCFReader;
import com.edifecs.elm.ucf.domain.support.PropertyImpl;
import com.edifecs.correction.utility.FormInputItem;
import com.edifecs.correction.utility.CorrectionUIBean;

import java.util.Collection;
import java.util.Map;
import java.io.File;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelSave {
	final Logger logger = LoggerFactory.getLogger(ModelSave.class);
	
	public UCFWriter<Model> getModelWriter(){
		return  UCFFactory.getInstance().getWriter();
	}
	
	
	public String save(HttpSession session, Map <Integer, FormInputItem>formInput) {
		try {
			CorrectionUIBean uibean = (CorrectionUIBean) session.getAttribute("uiBean");
			String xmlId = uibean.getXmlId();
			String status = "Correction Saved. \n(May be viewed at c:/"+xmlId;
		
			Map <Integer, Property> propertyMap = (Map<Integer, Property>) session.getAttribute("propertymap");
			
			status = xmlId+" "+status;
			
			for(Integer key : propertyMap.keySet()) {
				Property p = (Property) propertyMap.get(key);
				if(formInput.containsKey(key)){
					String value = formInput.get(key).getInputValue();
					if(value == "") //TODO: this can be removed once Ron K. fixes in UCF..
						value=null;
					logger.info("updating:"+p.getPropertyName()); //+" with:"+(String)formInput.get(key));
					p.setPropertyValue(value);
				} else {
					logger.info(p.getPropertyName()+ " NOT FOUND. setting to null");
					p.setPropertyValue(null); //this is not working due to UCF issue.. Ron K. looking at. The original value is still there.
				}
			}
			UCFFactory factory = UCFFactory.getInstance();
			UCFWriter<Model> writer = factory.getWriter();
			writer.write(new File("c:/"+xmlId),(Model)session.getAttribute("model"));
			
			
			return status;
		}
		catch (Exception e) { 
			e.printStackTrace();
			return "Error saving Correction:"+ e.getMessage();
		}
	}
}
