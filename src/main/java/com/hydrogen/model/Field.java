package com.hydrogen.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Field implements Serializable{

	private String name=null;
	private String type=null;
	private boolean required=false;
	private String value_by_header=null;
	private String format=null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getValue_by_header() {
		return value_by_header;
	}
	public void setValue_by_header(String value_by_header) {
		this.value_by_header = value_by_header;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	private List<Value> values = new ArrayList<Value>();
	
	public void addValue(Value obj){
		values.add(obj);
	}
	
	public List<Value> getValues(){
		return values;
	}
	@Override
	public String toString() {
		return "Field [name=" + name + ", type=" + type + ", required=" + required + ", value_by_header="
				+ value_by_header + ", format=" + format + ", values=" + values + "]";
	}
	
}
