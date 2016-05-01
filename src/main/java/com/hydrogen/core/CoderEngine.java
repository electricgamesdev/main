package com.hydrogen.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

public class HydrogenEngine {

	public void build() {
		for (Object key : econfig.keySet()) {
			try {
				Class c = Class.forName(econfig.getProperty(key.toString()));
				Engine e = (Engine) c.newInstance();
				e.setContext(context);
				e.build();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void execute() {
		for (Object key : econfig.keySet()) {
			try {
				Class c = Class.forName(econfig.getProperty(key.toString()));
				Engine e = (Engine) c.newInstance();
				e.setContext(context);
				e.execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private HydrogenEngine engine = null;
	private Properties econfig = new Properties();

	private HydrogenEngine(HydridesContext context) {
		this.context = context;
		try {
			econfig.load(Engine.class.getResourceAsStream("engine.properties"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static HydrogenEngine getEngine(HydridesContext context) {
		return new HydrogenEngine(context);
	}

	private HydridesContext context;

	public HydridesContext getContext() {
		return context;
	}

}
