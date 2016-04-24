package com.hydrogen.core;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
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
import com.hydrogen.stage.Stage.TYPE;
import com.hydrogen.stage.Validation;

public class StageManager {

	List<Worker> pool = new ArrayList<Worker>();
	ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(6);

	public StageManager() {

	}

	public void start() throws InterruptedException {

		addWorker(new IngestionWorker(this));
		addWorker(new ValidationWorker(this));
		addWorker(new AnalyticsWorker(this));
		addWorker(new DimensionWorker(this));
		addWorker(new FormatingWorker(this));
		addWorker(new PresentationWorker(this));

	}

	public List<Runnable> stop() {
		return scheduledThreadPool.shutdownNow();
	}

	private void addWorker(Worker worker) {
		ScheduledFuture f = scheduledThreadPool.scheduleAtFixedRate(worker, 10, 10, TimeUnit.SECONDS);
		pool.add(worker);
	}

	public void nextPhase(TYPE currentPhase, List<Stage> dataset) {

		if (Stage.TYPE.INGESTION.equals(currentPhase)) {

			startValidation(dataset);
		}

		if (Stage.TYPE.VALIDATION.equals(currentPhase)) {
			// run crunch jobs
			startAnalytics(dataset);
		}
		if (Stage.TYPE.ANALYTICS.equals(currentPhase)) {
			// populate data to hbase
			startDimension(dataset);
		}

		if (Stage.TYPE.PRESENTATION.equals(currentPhase)) {
			// formating to stream data using spark
			startFormating(dataset);
		}
		if (Stage.TYPE.FORMATION.equals(currentPhase)) {
			// watch and connect with servlet for faster rendering
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

		StageManager manager = new StageManager();
		manager.start();
	}

	public String getEnv(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public void error(Worker worker, Throwable th) {
		th.printStackTrace();

	}

}
