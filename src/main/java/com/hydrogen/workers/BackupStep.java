package com.hydrogen.steps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.oozie.client.CoordinatorJob;
import org.apache.oozie.client.Job.Status;
import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;

import com.hydrogen.core.StepManager;
import com.hydrogen.core.Step;
import com.hydrogen.jpa.DBUtil;
import com.hydrogen.model.stage.Stage;
import com.hydrogen.model.stage.Validation;

public class BackupStep extends Step {

	private Map<String, Object> where = null;

	public BackupStep(StepManager manager) {
		super(manager);
		this.where = new HashMap<String, Object>();
		where.put("status", Stage.STATUS.INIT);
	}

	public void work() {
		
			List<Validation> running = DBUtil.findAll(Validation.class, where);
			if ( running != null && running.size() > 0)
				process(null);
		
	}

	private void process(List<Validation> ilist) {
		try {
			OozieClient wc = new OozieClient(getManager().getEnv("oozie-url"));
			List<Stage> nlist = new ArrayList<Stage>();

			for (Validation validation : ilist) {
				CoordinatorJob co = wc.getCoordJobInfo(validation.getRefId());
				if (co.getStatus() == Status.RUNNING) {
					validation.setStatus(Stage.STATUS.RUNNING);
					if (validation.isStatusChanged()) {
						DBUtil.merge(validation);
					}
				} else if (co.getStatus() == Status.SUCCEEDED) {
					nlist.add(validation);
				} else if (co.getStatus() == Status.FAILED || co.getStatus() == Status.KILLED
						|| co.getStatus() == Status.RUNNINGWITHERROR || co.getStatus() == Status.IGNORED
						|| co.getStatus() == Status.DONEWITHERROR) {
					validation.setStatus(Stage.STATUS.ERROR);
					validation.setErrors(co.getStatus().toString());
					validation.setLog("External Id:" + co.getExternalId() + "\n URL:" + co.getConsoleUrl());
					DBUtil.merge(validation);
				}
			}

			if (nlist.size() > 0) {
				getManager().nextPhase(Stage.TYPE.VALIDATION, nlist);
			}
		} catch (OozieClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
	}

}
