package com.hydrogen.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Hydrides extends Model {

	@OneToMany(mappedBy = "hydrides")
	private List<Source> sources = new ArrayList<Source>();

	@OneToMany(mappedBy = "hydrides")
	private List<Template> components = new ArrayList<Template>();

	@OneToMany(mappedBy = "hydrides")
	private List<Workflow> workflows = new ArrayList<Workflow>();

	private String domainPath = null;
	private String prototypePath = null;
	private String piplinePath = null;

	public String getDomainPath() {
		return domainPath;
	}

	public void setDomainPath(String domainPath) {
		this.domainPath = domainPath;
	}

	public String getPrototypePath() {
		return prototypePath;
	}

	public void setPrototypePath(String prototypePath) {
		this.prototypePath = prototypePath;
	}

	public String getPiplinePath() {
		return piplinePath;
	}

	public void setPiplinePath(String piplinePath) {
		this.piplinePath = piplinePath;
	}

	public void addWorkflow(Workflow obj) {
		workflows.add(obj);
	}

	public List<Workflow> getWorkflows() {
		return workflows;
	}

	public void addComponent(Template obj) {
		components.add(obj);
	}

	public List<Template> getComponents() {
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
		return "Hydrides [sources=" + sources + ", components=" + components + ", workflows=" + workflows
				+ ", domainPath=" + domainPath + ", prototypePath=" + prototypePath + ", piplinePath=" + piplinePath
				+ "]";
	}

}
