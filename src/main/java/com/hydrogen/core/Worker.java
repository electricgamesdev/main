package com.hydrogen.core;

public abstract class Step implements Runnable {

	private StepManager manager = null;

	public Step(StepManager manager) {
		this.manager = manager;
	}
	
	public StepManager getManager() {
		return manager;
	}

	public void run() {
		try {
			System.out.println(getClass().getSimpleName() + " started");
			work();
			System.out.println(getClass().getSimpleName() + " completed");
		} catch (Throwable e) {
			manager.error(this,e);
		}
	}

	public abstract void setup();
	public abstract void work();

	public void done() {
		System.out.println(getClass().getSimpleName() + " done");

	}
}
