package com.hydrogen.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;
import org.apache.oozie.client.WorkflowJob;
import org.apache.oozie.client.WorkflowJob.Status;

import com.hydrogen.jpa.DBUtil;
import com.hydrogen.stage.Analytics;
import com.hydrogen.stage.Stage;

public class AnalyticsWorker extends Worker implements Runnable {

	private StageManager manager = null;
	private Map<String, Object> where = null;

	public AnalyticsWorker(StageManager manager) {
		this.manager = manager;
		this.where = new HashMap<String, Object>();
		where.put("status", Stage.STATUS.INIT);
	}

	public void run() {
		List<Analytics> running = DBUtil.findAll(Analytics.class, where);
		process(running);
	}

	private void process(List<Analytics> ilist) {
		try {
			OozieClient wc = new OozieClient(manager.getEnv("oozie-url"));
			List<Stage> nlist = new ArrayList<Stage>();

			for (Analytics analysis : ilist) {
				WorkflowJob co = wc.getJobInfo(analysis.getRefId());
				if (co.getStatus() == Status.RUNNING) {
					analysis.setStatus(Stage.STATUS.RUNNING);
					if (analysis.isStatusChanged()) {
						DBUtil.merge(analysis);
					}
				} else if (co.getStatus() == Status.SUCCEEDED) {
					nlist.add(analysis);
				} else if (co.getStatus() == Status.FAILED || co.getStatus() == Status.KILLED) {
					analysis.setStatus(Stage.STATUS.ERROR);
					analysis.setErrors(co.getStatus().toString());
					analysis.setLog("External Id:" + co.getExternalId() + "\n URL:" + co.getConsoleUrl());
					DBUtil.merge(analysis);
				}
			}

			if (nlist.size() > 0) {
				manager.nextPhase(Stage.PHASE.CRUNCH, nlist);
			}
		} catch (OozieClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

	}

}
