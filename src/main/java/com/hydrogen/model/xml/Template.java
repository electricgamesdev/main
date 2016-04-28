package com.hydrogen.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Template extends Model {

	private List<Workflow> workflows = new ArrayList<Workflow>();
	
	@OneToMany(mappedBy="template")
	private List<Form> forms = new ArrayList<Form>();

	@ManyToOne
	private Hydrides hydrides = null;
	
	public Hydrides getHydrides() {
		return hydrides;
	}
	
	public void setHydrides(Hydrides hydrides) {
		this.hydrides = hydrides;
	}
	
	public List<Form> getForms() {
		return forms;
	}
	
	public void addForm(Form f){
		forms.add(f);
	}
	
	public void addWorkflow(Workflow obj) {
		workflows.add(obj);
	}

	public Workflow getWorkflow(String path) {
		return null;
	}


	@Override
	public String toString() {
		return "Component [path=" + getPath() + ", link=" + getLink() + ", workflows=" + workflows + ", forms=" + forms + "]";
	}

	

	
	
}
