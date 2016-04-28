package com.hydrogen.model.stage;


import javax.persistence.ManyToOne;

import com.hydrogen.model.xml.Entity;
import com.hydrogen.model.xml.Source;

@javax.persistence.Entity
public class Ingestion extends Stage {
	
	@ManyToOne
	private Source source=null;
	
	@ManyToOne
	private Entity entity=null;

	private String groupKey = null;
	
	public String getGroupKey() {
		return groupKey;
	}
	
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}
	
	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	

}
