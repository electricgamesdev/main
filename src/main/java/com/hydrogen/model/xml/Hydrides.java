package com.hydrogen.model.xml;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Hydrides extends Model {

	@OneToMany(mappedBy = "hydrides")
	private List<Source> sources = new ArrayList<Source>();

	@OneToMany(mappedBy = "hydrides")
	private List<Template> templates = new ArrayList<Template>();

	@OneToMany(mappedBy = "hydrides")
	private List<Workflow> workflows = new ArrayList<Workflow>();

	private String domainPath = null;
	private String prototypePath = null;
	private String pipelinePath = null;

	private String domainType=null;
	private String prototypeType = null;
	private String pipelineType = null;
	
	private String namespace = null;
	
	public String getNamespace() {
		return namespace;
	}
	
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public String getDomainType() {
		return domainType;
	}

	public void setDomainType(String domainType) {
		this.domainType = domainType;
	}

	public String getPrototypeType() {
		return prototypeType;
	}

	public void setPrototypeType(String prototypeType) {
		this.prototypeType = prototypeType;
	}

	public String getPipelineType() {
		return pipelineType;
	}

	public void setPipelineType(String piplineType) {
		this.pipelineType = piplineType;
	}

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

	public String getPipelinePath() {
		return pipelinePath;
	}

	public void setPipelinePath(String piplinePath) {
		this.pipelinePath = piplinePath;
	}

	public void addWorkflow(Workflow obj) {
		workflows.add(obj);
	}

	public List<Workflow> getWorkflows() {
		return workflows;
	}

	public void addTemplate(Template obj) {
		templates.add(obj);
	}

	public List<Template> getTemplates() {
		return templates;
	}

	public void addSource(Source source) {
		sources.add(source);
	}

	public List<Source> getSources() {
		return sources;
	}

	@Override
	public String toString() {
		return "Hydrides [sources=" + sources + ", components=" + templates + ", workflows=" + workflows
				+ ", domainPath=" + domainPath + ", prototypePath=" + prototypePath + ", piplinePath=" + pipelinePath
				+ "]";
	}

}
