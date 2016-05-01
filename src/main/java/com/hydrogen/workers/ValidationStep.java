package com.hydrogen.workers;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.oozie.client.CoordinatorJob;
import org.apache.oozie.client.Job.Status;
import org.apache.oozie.client.OozieClient;
import org.apache.oozie.client.OozieClientException;

import com.hydrogen.core.StepManager;
import com.hydrogen.core.Worker;
import com.hydrogen.jpa.DBUtil;
import com.hydrogen.model.step.Ingestion;
import com.hydrogen.model.step.Step;
import com.hydrogen.model.step.Validation;

public class ValidationStep extends Worker {

	public ValidationStep(StepManager manager) {
		super(manager);

	}

	public void where(Map<String, Object> where) {
		where.put("status", Step.STATUS.INIT);
	}

	public Class getModelClass() {
		return Validation.class;
	}

	public void work(List<Step> ilist) {

		try {

			OozieClient wc = new OozieClient(getManager().getEnv("oozie-url"));
			List<Step> nlist = new ArrayList<Step>();

			for (Step s : ilist) {
				Validation validation = (Validation) s;
				CoordinatorJob co = wc.getCoordJobInfo(validation.getRefId());
				if (co.getStatus() == Status.RUNNING) {
					validation.setStatus(Step.STATUS.RUNNING);
					if (validation.isStatusChanged()) {
						DBUtil.merge(validation);
					}
				} else if (co.getStatus() == Status.SUCCEEDED) {
					nlist.add(validation);
				} else if (co.getStatus() == Status.FAILED || co.getStatus() == Status.KILLED
						|| co.getStatus() == Status.RUNNINGWITHERROR || co.getStatus() == Status.IGNORED
						|| co.getStatus() == Status.DONEWITHERROR) {
					validation.setStatus(Step.STATUS.ERROR);
					validation.setErrors(co.getStatus().toString());
					validation.setLog("External Id:" + co.getExternalId() + "\n URL:" + co.getConsoleUrl());
					DBUtil.merge(validation);
				}
			}

			if (nlist.size() > 0) {
				getManager().nextStep(this, nlist);
			}
		} catch (OozieClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

	}

	@Override
	public void setup() {
		

	}

	@Override
	public void create(List<Step> dataset) {
		Validation validation = new Validation();
		try {
			Properties p = new Properties();
			Ingestion master = (Ingestion) dataset.get(0);

			validation.setSource(master.getSource());
			validation.setStatus(Step.STATUS.INIT);
			validation.setStartTime(DBUtil.now());
			p.load(new FileReader(System.getenv("user.home") + File.separator + "hydrogen" + File.separator
					+ master.getSource().getName() + ".properties"));
			for (Step s : dataset) {
				Ingestion ing = (Ingestion) s;
				p.setProperty(ing.getEntity().getName(), ing.getName());
			}
			OozieClient wc = new OozieClient(p.getProperty("oozie.url"));

			// create a workflow job configuration and set the workflow
			// application path
			Properties conf = wc.createConfiguration();
			for (Object key : p.keySet()) {
				conf.put(key, p.getProperty(key.toString()));
			}

			// submit and start the workflow job
			String jobId = wc.run(conf);
			validation.setRefId(jobId);

		} catch (Exception e) {
			validation.setErrors(e.getMessage());
			validation.setLog(e.toString());
			validation.setStatus(Step.STATUS.ERROR);
		} finally {

			DBUtil.persist(validation);
			for (Step s : dataset) {
				Ingestion ing = (Ingestion) s;
				s.setNextPhase(validation);
				DBUtil.merge(ing);
			}
		}

	}

}
