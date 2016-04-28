package com.hydrogen.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ManyToOne;

@javax.persistence.Entity
public class Form extends Model {

	private Entity filter = null;

	private List<Script> scripts = new ArrayList<Script>();

	private List<Event> events = new ArrayList<Event>();

	@ManyToOne
	private Template template = null;

	

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void addEvent(Event e) {
		this.events.add(e);
	}

	public List<Script> getScripts() {
		return scripts;
	}

	public void addScript(Script script) {
		scripts.add(script);
	}

	public Entity getFilter() {
		return filter;
	}

	public void setFilter(Entity filter) {
		this.filter = filter;
	}

	@Override
	public String toString() {
		return "Form [path=" + getPath() + ", ref=" + getRef() + ", as=" + getAs() + ", filter=" + filter + ", scripts="
				+ scripts + "]";
	}

}
