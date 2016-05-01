package com.hydrogen.core;

public  interface Engine {

	public void build();
	
	public void execute();
	

	
	public void setContext(HydridesContext context);

	public String getName();
}
