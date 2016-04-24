package com.hydrogen.core;

public abstract class Worker implements Runnable {

	private StageManager manager = null;

	public Worker(StageManager manager) {
		this.manager = manager;
	}
	
	public StageManager getManager() {
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

	public abstract void work();

	public void done() {
		System.out.println(getClass().getSimpleName() + " done");

	}
}
