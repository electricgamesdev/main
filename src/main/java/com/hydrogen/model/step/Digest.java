package com.hydrogen.model.step;


import javax.persistence.ManyToOne;

import com.hydrogen.model.xml.Entity;
import com.hydrogen.model.xml.Source;

@javax.persistence.Entity
public class Digest extends Step {
	
	@ManyToOne
	private Source source=null;
	
	public Source getSource() {
		return source;
	}
	
	public void setSource(Source source) {
		this.source = source;
	}
	

}
