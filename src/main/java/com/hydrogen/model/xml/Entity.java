package com.hydrogen.model.xml;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ManyToOne;

@javax.persistence.Entity
public class Entity extends Model {

	
	private String pattern = null;
	private String field_splitter=null;
	private String record_splitter=null;
	private String format=null;
	private String value_from_header=null;
	private String entity=null;
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

	
	public void addValue(Field obj) {
		fields.add(obj);
	}

	public List<Field> getValues() {
		return fields;
	}

	public String getField_splitter() {
		return field_splitter;
	}

	public void setField_splitter(String field_splitter) {
		this.field_splitter = field_splitter;
	}

	public String getRecord_splitter() {
		return record_splitter;
	}

	public void setRecord_splitter(String record_splitter) {
		this.record_splitter = record_splitter;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getValue_from_header() {
		return value_from_header;
	}

	public void setValue_from_header(String value_from_header) {
		this.value_from_header = value_from_header;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void addField(Field f) {
		this.fields.add(f);
	}

	@Override
	public String toString() {
		return "Entity [pattern=" + pattern + ", field_splitter=" + field_splitter + ", record_splitter="
				+ record_splitter + ", format=" + format + ", value_from_header=" + value_from_header + ", entity="
				+ entity + ", fields=" + fields + ", source=" + source + "]";
	}


	
}
