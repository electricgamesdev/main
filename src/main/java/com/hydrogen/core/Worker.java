package com.hydrogen.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hydrogen.jpa.DBUtil;
import com.hydrogen.model.step.Step;
import com.hydrogen.model.step.Validation;
import com.hydrogen.model.util.Technology;

public abstract class Worker implements Runnable {

	private StepManager manager = null;
	private Integer id = null;
	private Engine engine = null;
	private Map<String, Object> where = null;

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Worker(StepManager manager) {
		this.manager = manager;
	}

	public StepManager getManager() {
		return manager;
	}

	public void where(Map<String, Object> where) {

	}
	
	public Class getModelClass(){
		return Validation.class;
	}

	public void run() {
		try {
			System.out.println(getClass().getSimpleName() + " started");
			where = new HashMap<String, Object>();
			where(where);
			work(DBUtil.findAll(getModelClass(), where));
			System.out.println(getClass().getSimpleName() + " completed");
		} catch (Throwable e) {
			manager.error(this, e);
		}
	}

	public void setup(){
		Technology technology = new Technology();
		setup(technology);
		DBUtil.merge(technology);
	}
	
	public void setup(Technology technology){
		
	}

	public abstract void work(List<Step> list) throws Exception;

	public void done() {
		System.out.println(getClass().getSimpleName() + " done");

	}

	public void setContext(HydridesContext context) {
		// TODO Auto-generated method stub

	}

	public abstract void create(List<Step> dataset);
}
