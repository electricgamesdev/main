package com.hydrogen.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public abstract class AbstractEngine implements Engine {

	private HydridesContext context = null;
	private Map<String, String> configMap = new HashMap<String, String>();

	public HydridesContext getContext() {
		return context;
	}

	public void setContext(HydridesContext context) {
		this.context = context;
	}

	public void addConfig(String key, String value) {
		this.configMap.put(key, value);
	}

	public String getConfig(String key) {
		return configMap.get(key);
	}

	public Map<String, String> getConfigMap() {
		return configMap;
	}

	public void writeCode(String filename, String content) {

		try {
			File f=context.getFileToCreate(getName()+File.separator+filename);
			FileUtils.writeStringToFile(f, content);
			context.log("Created "+f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
