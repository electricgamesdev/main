package com.hydrogen.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ManyToOne;

@javax.persistence.Entity
public class Entity extends Model {

	
	private String pattern = null;
	private String field_delimeter = null;
	private String line_delimeter = "line";
	private List<Field> fields = new ArrayList<Field>();
	
	@ManyToOne
	private Source source = null;

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getField_delimeter() {
		return field_delimeter;
	}

	public void setField_delimeter(String field_delimeter) {
		this.field_delimeter = field_delimeter;
	}

	public String getLine_delimeter() {
		return line_delimeter;
	}

	public void setLine_delimeter(String line_delimeter) {
		this.line_delimeter = line_delimeter;
	}

	public void addValue(Field obj) {
		fields.add(obj);
	}

	public List<Field> getValues() {
		return fields;
	}

	@Override
	public String toString() {
		return "Entity [path=" + getPath() + ", as=" + getAs() + ", type=" + getType() + ", ref=" + getRef()
				+ ", pattern=" + pattern + ", field_delimeter=" + field_delimeter + ", line_delimeter=" + line_delimeter
				+ ", fields=" + fields + "]";
	}

}
