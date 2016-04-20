package com.hydrogen.model;

import java.util.ArrayList;
import java.util.List;

public class Event {

	private String as=null;
	private String type=null;
	private List<Param> params = new ArrayList<Param>();
	public String getAs() {
		return as;
	}
	public void setAs(String as) {
		this.as = as;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Param> getParams() {
		return params;
	}
	public void addParam(Param p) {
		this.params.add(p);
	}
	@Override
	public String toString() {
		return "Event [as=" + as + ", type=" + type + ", params=" + params + "]";
	}
	
	
	

}
