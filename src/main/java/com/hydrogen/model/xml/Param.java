package com.hydrogen.model.xml;

public class Param {

	private String name=null;
	private String as=null;
	private String value=null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAs() {
		return as;
	}
	public void setAs(String as) {
		this.as = as;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Param [name=" + name + ", as=" + as + ", value=" + value + "]";
	}
	
	
	
}
