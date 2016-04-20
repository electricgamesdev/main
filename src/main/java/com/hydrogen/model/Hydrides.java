package com.hydrogen.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Entity
public class Hydrides extends Model {

	@OneToMany(mappedBy = "hydrides")
	private List<Source> sources = new ArrayList<Source>();
	
	@OneToMany(mappedBy = "hydrides")
	private List<Component> components = new ArrayList<Component>();
	
	@OneToMany(mappedBy = "hydrides")
	private List<Workflow> workflows = new ArrayList<Workflow>();

	public void addWorkflow(Workflow obj) {
		workflows.add(obj);
	}

	public List<Workflow> getWorkflows() {
		return workflows;
	}

	public void addComponent(Component obj) {
		components.add(obj);
	}

	public List<Component> getComponents() {
		return components;
	}

	public void addSource(Source source) {
		sources.add(source);
	}

	public List<Source> getSources() {
		return sources;
	}

	@Override
	public String toString() {
		return "Hydrides [path=" + getPath() + ", sources=" + sources + ", components=" + components + ", workflows="
				+ workflows + "]";
	}

}
