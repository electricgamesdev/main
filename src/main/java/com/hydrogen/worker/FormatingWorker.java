package com.hydrogen.worker;

import java.util.HashMap;
import java.util.Map;

import com.hydrogen.core.StageManager;
import com.hydrogen.core.Worker;
import com.hydrogen.stage.Stage;

public class FormatingWorker extends Worker {


	private Map<String, Object> where = null;

	public FormatingWorker(StageManager manager) {
		super(manager);
		this.where = new HashMap<String, Object>();
		where.put("status", Stage.STATUS.INIT);
	}

	public void work() {
		
	}

}
