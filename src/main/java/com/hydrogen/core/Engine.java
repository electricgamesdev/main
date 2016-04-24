package com.hydrogen.core;

import java.util.Map;

public  interface Engine {

	public void build();
	
	public void execute();
	
	public void setContext(HydridesContext context);

}
