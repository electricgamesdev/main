package com.hydrogen.model;

import java.io.Serializable;

public class Value implements Serializable{

		private String name=null;
		private String value=null;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		@Override
		public String toString() {
			return "Value [name=" + name + ", value=" + value + "]";
		}
		
		
}
