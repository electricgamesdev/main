package com.hydrogen.core;

import java.util.HashMap;
import java.util.Map;

import com.hydrogen.stage.Stage;

public class PresentationWorker extends Worker  {


	private Map<String, Object> where = null;

	public PresentationWorker(StageManager manager) {
		super(manager);
		this.where = new HashMap<String, Object>();
		where.put("status", Stage.STATUS.INIT);
	}

	public void work() {
		
	}
		
}
