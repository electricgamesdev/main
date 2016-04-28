package com.hydrogen.model.xml;

public class Script {

	private String type=null;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	private String script=null;
	
	public String getScript() {
		return script;
	}
	
	public void setScript(String script) {
		this.script = script;
	}
	@Override
	public String toString() {
		return "Script [type=" + type + ", script=" + script + "]";
	}
	
}
