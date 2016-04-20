package com.hydrogen.model;

import javax.persistence.Entity;

@Entity
public class Rules extends Model{

	@Override
	public String toString() {
		return "Rules [as=" + getAs() + ", path=" + getPath() + "]";
	}
	
	

}
