package com.hydrogen.workers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.oozie.client.CoordinatorJob;
import org.apache.oozie.client.Job.Status;
import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;

import com.hydrogen.core.StepManager;
import com.hydrogen.core.Worker;
import com.hydrogen.jpa.DBUtil;
import com.hydrogen.model.step.Step;
import com.hydrogen.model.step.Validation;

public class BackupStep extends Worker {

	private Map<String, Object> where = null;

	public BackupStep(StepManager manager) {
		super(manager);
		this.where = new HashMap<String, Object>();
		where.put("status", Step.STATUS.INIT);
	}

	public void work() {
		
			List<Validation> running = DBUtil.findAll(Validation.class, where);
			if ( running != null && running.size() > 0)
				process(null);
		
	}

	private void process(List<Validation> ilist) {
		try {
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

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
