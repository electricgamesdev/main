package com.hydrogen.core;

import java.util.HashMap;
import java.util.Map;

import com.hydrogen.stage.Stage;

public class DimensionWorker extends Worker  {

	private Map<String, Object> where = null;

	public DimensionWorker(StageManager manager) {
		super(manager);
		this.where = new HashMap<String, Object>();
		where.put("status", Stage.STATUS.INIT);
	}

	public void work() {
		
	}

}
