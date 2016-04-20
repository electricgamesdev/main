package com.hydrogen.stage;


import javax.persistence.ManyToOne;

import com.hydrogen.model.Entity;
import com.hydrogen.model.Source;

@javax.persistence.Entity
public class Validation extends Stage {
	
	@ManyToOne
	private Source source=null;
	
	public Source getSource() {
		return source;
	}
	
	public void setSource(Source source) {
		this.source = source;
	}
	

}
