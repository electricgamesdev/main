package com.hydrogen.steps;

import java.util.HashMap;
import java.util.Map;

import com.hydrogen.core.StepManager;
import com.hydrogen.core.Step;
import com.hydrogen.model.stage.Stage;

public class PresentationStep extends Step  {


	private Map<String, Object> where = null;

	public PresentationStep(StepManager manager) {
		super(manager);
		this.where = new HashMap<String, Object>();
		where.put("status", Stage.STATUS.INIT);
	}

	public void work() {
		
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
	}
		
}
