package com.hydrogen.core;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.apache.oozie.client.OozieClient;

import com.hydrogen.jpa.DBUtil;
import com.hydrogen.jpa.ObjUtil;
import com.hydrogen.stage.Dimension;
import com.hydrogen.stage.Ingestion;
import com.hydrogen.stage.Stage;
import com.hydrogen.stage.Stage.PHASE;
import com.hydrogen.stage.Validation;

public class StageManager {

	List<Worker> pool = new ArrayList<Worker>();
	ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

	public StageManager() {

	}

	public void start() throws InterruptedException {

		Thread.sleep(1000);
		IngestionWorker ingestionWorker = new IngestionWorker(this);
		scheduledThreadPool.schedule(ingestionWorker, 10, TimeUnit.SECONDS);
		pool.add(ingestionWorker);

		Thread.sleep(1000);
		ValidationWorker validationWorker = new ValidationWorker(this);
		scheduledThreadPool.schedule(validationWorker, 10, TimeUnit.SECONDS);
		pool.add(validationWorker);

		Thread.sleep(1000);
		AnalyticsWorker analyticsWorker = new AnalyticsWorker(this);
		scheduledThreadPool.schedule(analyticsWorker, 10, TimeUnit.SECONDS);
		pool.add(analyticsWorker);

		Thread.sleep(1000);
		DimensionWorker dimensionWorker = new DimensionWorker(this);
		scheduledThreadPool.schedule(dimensionWorker, 10, TimeUnit.SECONDS);
		pool.add(dimensionWorker);

		Thread.sleep(1000);
		FormatingWorker formatingWorker = new FormatingWorker(this);
		scheduledThreadPool.schedule(formatingWorker, 10, TimeUnit.SECONDS);
		pool.add(formatingWorker);

		Thread.sleep(1000);
		PresentationWorker presentationWorker = new PresentationWorker(this);
		scheduledThreadPool.schedule(presentationWorker, 10, TimeUnit.SECONDS);
		pool.add(presentationWorker);

	}

	public void nextPhase(PHASE currentPhase, List<Stage> dataset) {

		if (Stage.PHASE.FLUME.equals(currentPhase)) {

			startValidation(dataset);
		}

		if (Stage.PHASE.OOZIE.equals(currentPhase)) {
			// run crunch jobs
			startAnalytics(dataset);
		}
		if (Stage.PHASE.CRUNCH.equals(currentPhase)) {
			// populate data to hbase
			startDimension(dataset);
		}

		if (Stage.PHASE.HBASE.equals(currentPhase)) {
			//formating to stream data using spark
			startFormating(dataset);
		}
		if (Stage.PHASE.SPARK.equals(currentPhase)) {
			//watch and connect with servlet for faster rendering
			startPresentation(dataset);
		}

	}

	private void startPresentation(List<Stage> dataset) {

	}

	private void startFormating(List<Stage> dataset) {

	}

	private void startDimension(List<Stage> dataset) {

	}

	private void startAnalytics(List<Stage> dataset) {

	}

	private void startValidation(List<Stage> dataset) {

		Validation validation = new Validation();
		try {
			Properties p = new Properties();
			Ingestion master = (Ingestion) dataset.get(0);

			validation.setSource(master.getSource());
			validation.setStatus(Stage.STATUS.INIT);
			validation.setStartTime(ObjUtil.now());
			p.load(new FileReader(System.getenv("user.home") + File.separator + "hydrogen" + File.separator
					+ master.getSource().getName() + ".properties"));
			for (Stage s : dataset) {
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
			validation.setStatus(Stage.STATUS.ERROR);
		} finally {

			DBUtil.persist(validation);
			for (Stage s : dataset) {
				Ingestion ing = (Ingestion) s;
				s.setNextPhase(validation);
				DBUtil.merge(ing);
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {

	}

	public String getEnv(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
