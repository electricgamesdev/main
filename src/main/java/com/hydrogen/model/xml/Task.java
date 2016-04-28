package com.hydrogen.model;

import java.util.ArrayList;
import java.util.List;

public class Task {

	private String type = null;
	private List<Entity> entities = new ArrayList<Entity>();
	private List results = new ArrayList();
	private String condition = null;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void addEntities(Entity e) {
		this.entities.add(e);
	}

	public List getResults() {
		return results;
	}

	public void addResults(Form obj) {
		this.results.add(obj);
	}

	public void addResults(Entity obj) {
		this.results.add(obj);
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "Task [type=" + type + ", entities=" + entities + ", results=" + results + ", condition=" + condition
				+ "]";
	}

}
