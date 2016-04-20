package com.hydrogen.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.oozie.client.CoordinatorJob;
import org.apache.oozie.client.Job.Status;
import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;

import com.hydrogen.jpa.DBUtil;
import com.hydrogen.stage.Stage;
import com.hydrogen.stage.Validation;

public class PresentationWorker extends Worker implements Runnable {

	private StageManager manager = null;
	private Map<String, Object> where = null;

	public PresentationWorker(StageManager manager) {
		this.manager = manager;
		this.where = new HashMap<String, Object>();
		where.put("status", Stage.STATUS.INIT);
	}

	public void run() {
		List<Validation> running = DBUtil.findAll(Validation.class, where);
		process(running);
	}

	private void process(List<Validation> ilist) {
		try {
			OozieClient wc = new OozieClient(manager.getEnv("oozie-url"));
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
				manager.nextPhase(Stage.PHASE.OOZIE, nlist);
			}
		} catch (OozieClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

	}

}
