package com.hydrogen.core;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hydrogen.model.step.Step;

public class StepManager {
	private Log log = LogFactory.getLog(StepManager.class);

	Map<Integer, Worker> pool = new HashMap<Integer, Worker>();
	ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(6);
	private Properties wconfig = new Properties();
	private Properties econfig = new Properties();

	public StepManager() {
		try {
			wconfig.load(Worker.class.getResourceAsStream("worker.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() throws InterruptedException {
		for (Object key : wconfig.keySet()) {
			try {
				Class c = Class.forName(wconfig.getProperty(key.toString()));
				Constructor<Worker> c2 = c.getConstructor(StepManager.class);
				Worker w = (Worker) c2.newInstance(this);
				// e.setContext(context);
				log.info("Starting " + w.getClass().getSimpleName());
				pool.put(Integer.parseInt(key.toString()), w);
				w.setId(Integer.parseInt(key.toString()));
				startWorker(w);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public List<Runnable> stop() {
		return scheduledThreadPool.shutdownNow();
	}

	private void startWorker(Worker worker) {
		ScheduledFuture f = scheduledThreadPool.scheduleAtFixedRate(worker, 10, 10, TimeUnit.SECONDS);

	}

	public void nextStep(Worker wold, List<Step> dataset) {

		Worker wnew = pool.get(wold.getId() + 1);
		wnew.create(dataset);
	}

	public static void main(String[] args) throws InterruptedException {

		StepManager manager = new StepManager();
		manager.start();
	}

	public String getEnv(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public void error(com.hydrogen.core.Worker step, Throwable th) {
		th.printStackTrace();

	}

}
