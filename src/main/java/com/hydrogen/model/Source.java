package com.hydrogen.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@javax.persistence.Entity
public class Source extends Model {

	private List<Workflow> workflows = new ArrayList<Workflow>();

	private String hdfs_path;
	private String group_filter;
	
	@OneToMany(mappedBy="source")
	private List<Entity> entities = new ArrayList<Entity>();
	
	@ManyToOne
	private Hydrides hydrides = null;
	
	public Hydrides getHydrides() {
		return hydrides;
	}
	
	public void setHydrides(Hydrides hydrides) {
		this.hydrides = hydrides;
	}
	
	public List<Entity> getEntities() {
		return entities;
	}

	public void addEntity(Entity e) {
		this.entities.add(e);
	}

	public void addWorkflow(Workflow obj) {
		workflows.add(obj);
	}

	public List<Workflow> getWorkflows() {
		return workflows;
	}

	public String getHdfs_path() {
		return hdfs_path;
	}

	public void setHdfs_path(String hdfs_path) {
		this.hdfs_path = hdfs_path;
	}

	public String getGroup_filter() {
		return group_filter;
	}

	public void setGroup_filter(String group_filter) {
		this.group_filter = group_filter;
	}

	@Override
	public String toString() {
		return "Source [path=" + getPath() + ", link=" + getLink() + ", workflows=" + workflows + ", hdfs_path=" + hdfs_path
				+ ", group_filter=" + group_filter + ", entities=" + entities + "]";
	}

}
