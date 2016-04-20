package com.hydrogen.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ManyToOne;

import com.hydrogen.jpa.DBUtil;

@javax.persistence.Entity
public class Action extends Model{

	
	private String depends=null;
	private List<Form> forms = new ArrayList<Form>();
	private List<Entity> entities=new ArrayList<Entity>();
	private List<Rules> rules = new ArrayList<Rules>();
	private List<Task> tasks = new ArrayList<Task>();
	
	@ManyToOne
	private Workflow workflow=null;
	public Workflow getWorkflow() {
		return workflow;
	}
	
	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}
	
	public String getDepends() {
		return depends;
	}
	public void setDepends(String depends) {
		this.depends = depends;
	}
	
	public List<Task> getTasks() {
		return tasks;
	}
	public void addTasks(Task e) {
		this.tasks.add(e);
	}
	public List<Form> getForms() {
		return forms;
	}
	public void addForm(Form f) {
		this.forms.add(f);
	}
	public List<Entity> getEntities() {
		return entities;
	}
	public void addEntity(Entity e) {
		this.entities.add(e);
	}
	public List<Rules> getRules() {
		return rules;
	}
	public void addRules(Rules rule) {
		this.rules.add(rule);
	}
	@Override
	public String toString() {
		return "Action [path=" + getPath() + ", as=" + getAs() + ", depends=" + depends + ", type=" + getType() + ", forms=" + forms
				+ ", entities=" + entities + ", rules=" + rules + ", tasks=" + tasks + "]";
	}
	
	
	
	
	
	

}
