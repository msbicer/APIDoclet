package com.apidoclet.model;

public class Parameter implements Element {
	private String name;
	private String defaultValue;
	private String description;
	private String type;
	private boolean required;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Parameter [name=" + name + ", defaultValue=" + defaultValue
				+ ", description=" + description + ", type=" + type
				+ ", required=" + required + "]";
	}

	
}
