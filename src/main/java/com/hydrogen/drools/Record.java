package com.hydrogen.drools;

import java.util.HashMap;
import java.util.Map;

public class Record {
	
	Map<String,Object> map = new HashMap<String, Object>();
	
	public void put(String key,Object value){
		map.put(key, value);
	}
	
	public Object get(String key){
		return map.get(key);
	}

	@Override
	public String toString() {
		return "Record [map=" + map + "]";
	}

	
}
