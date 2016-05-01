package com.hydrogen.workers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;
import org.apache.oozie.client.WorkflowJob;
import org.apache.oozie.client.WorkflowJob.Status;

import com.hydrogen.core.StepManager;
import com.hydrogen.core.Worker;
import com.hydrogen.jpa.DBUtil;
import com.hydrogen.model.step.Analytics;
import com.hydrogen.model.step.Step;

public class DiscoverStep extends Worker {

	private Map<String, Object> where = null;

	public DiscoverStep(StepManager manager) {
		super(manager);
		this.where = new HashMap<String, Object>();
		where.put("status", Step.STATUS.INIT);
	}

	public void work() {
	
		List<Analytics> running = DBUtil.findAll(Analytics.class, where);
		process(running);
	}

	private void process(List<Analytics> ilist) {
	
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
