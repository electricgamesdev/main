package com.hydrogen.workers;

import java.util.List;
import java.util.Map;

import com.hydrogen.core.StepManager;
import com.hydrogen.core.Worker;
import com.hydrogen.model.step.Ingestion;
import com.hydrogen.model.step.Step;
import com.hydrogen.model.util.Technology;

public class DigestStep extends Worker {

	public DigestStep(StepManager manager) {
		super(manager);
	}

	@Override
	public void where(Map<String, Object> where) {
		where.put("status", Step.STATUS.RUNNING);
	}
	
	@Override
	public Class getModelClass() {
		return Ingestion.class;
	}

	public void work(List<Step> ls) {
		for (Step s : ls) {
			Ingestion ingestion = (Ingestion) s;
		}
		getManager().nextStep(this, ls);
	}

	@Override
	public void setup(Technology technology) {
		// start flume service
		
		technology.setName("FLUME");
		technology.setStatus("RUNNING");
		//getEngine().execute();
	}

	@Override
	public void create(List<Step> dataset) {
		// TODO Auto-generated method stub

	}

}
