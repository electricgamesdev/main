package com.hydrogen.core;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CoderEngine {
	private Log log = LogFactory.getLog(CoderEngine.class);
	
	public void build() {
		for (Object key : econfig.keySet()) {
			try {
				Class c = Class.forName(econfig.getProperty(key.toString()));
				Engine e = (Engine) c.newInstance();
				log.info("Building "+e.getName());
				e.setContext(context);
				e.build();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}


	
	
	private CoderEngine engine = null;
	private Properties econfig = new Properties();
	
	private CoderEngine(HydridesContext context) {
		this.context = context;
		try {
			econfig.load(Engine.class.getResourceAsStream("engine.properties"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static CoderEngine getEngine(HydridesContext context) {
		return new CoderEngine(context);
	}

	private HydridesContext context;

	public HydridesContext getContext() {
		return context;
	}
	
	

}
