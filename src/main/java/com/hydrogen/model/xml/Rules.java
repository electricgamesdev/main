package com.hydrogen.model.xml;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

@Entity
public class Rules extends Model{

	private List<Event> events = new ArrayList<Event>();
	
	public List<Event> getEvents() {
		return events;
	}
	
	public void addEvent(Event event) {
		this.events.add(event);
	}
	
	@Override
	public String toString() {
		return "Rules [as=" + getAs() + ", path=" + getPath() + "]";
	}
	
	

}
