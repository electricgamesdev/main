package com.hydrogen.workers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hydrogen.core.StepManager;
import com.hydrogen.core.Worker;
import com.hydrogen.model.step.Step;

public class PresentationStep extends Worker  {


	private Map<String, Object> where = null;

	public PresentationStep(StepManager manager) {
		super(manager);
		this.where = new HashMap<String, Object>();
		where.put("status", Step.STATUS.INIT);
	}

	public void work() {
		
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void create(List<Step> dataset) {
		// TODO Auto-generated method stub
		
	}
		
}
