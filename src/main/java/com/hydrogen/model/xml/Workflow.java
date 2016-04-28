package com.hydrogen.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@javax.persistence.Entity
public class Workflow extends Model{
	
	@OneToMany(mappedBy = "workflow")
	private List<Action> actions = new ArrayList<Action>();

	@ManyToOne
	private Hydrides hydrides = null;
	
	public Hydrides getHydrides() {
		return hydrides;
	}
	
	public void setHydrides(Hydrides hydrides) {
		this.hydrides = hydrides;
	}
	
	public List<Action> getActions() {
		return actions;
	}
	
	public void addAction(Action a) {
		this.actions.add(a);
	}
	
	
	@Override
	public String toString() {
		return "Workflow [link=" + getLink() + ", path=" + getPath() + ", actions=" + actions + "]";
	}
	
	
	

}
