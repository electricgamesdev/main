package com.hydrogen.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public abstract class HydrogenEngine extends Thread {
	public abstract void generateCode() throws IOException, HydridesContextException;

	public abstract void runProcess();

	private HydridesContext context;

	public void setContext(HydridesContext context) {
		this.context = context;
	}

	public HydridesContext getContext() {
		return context;
	}

	public Map<String, String> getConfigMap() {
		return configMap;
	}

	Map<String, String> configMap = new HashMap<String, String>();

	public void addConfig(String key, String value) {
		configMap.put(key, value);
	}

	public void writeCode(String filename, String content) throws IOException {

		FileUtils.writeStringToFile(context.getFileToCreate(filename), content);

	}
}
