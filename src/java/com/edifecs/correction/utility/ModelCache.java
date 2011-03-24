package com.edifecs.correction.utility;

import com.edifecs.correction.utility.ModelCacheEntry;
import java.util.Map;
import java.util.HashMap;

//import com.edifecs.elm.ucf.domain.Model;

public class ModelCache {
	private static Map <Integer, ModelCacheEntry> modelMap = new HashMap <Integer, ModelCacheEntry>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static ModelCacheEntry getModel(Integer key) {
		if(modelMap.containsKey(key)){
			return modelMap.get(key);
		}else {
			return null;
		}
	}
	
	public static void setModel(Integer key, ModelCacheEntry modelCacheEntry){
		modelMap.put(key, modelCacheEntry);
	}
	
	public static void removeModel(Integer key){
		if(modelMap.containsKey(key)){
			modelMap.remove(key);
		}
	}
	
}
