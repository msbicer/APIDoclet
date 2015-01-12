package com.apidoclet.model;

public class Parameter implements Element {
	private String name;
	private String defaultValue;
	private String description;
	private String type;
	private String qualifiedType;
	private boolean required;
	private boolean header;

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

	public String getQualifiedType() {
		return qualifiedType;
	}

	public void setQualifiedType(String qualifiedType) {
		this.qualifiedType = qualifiedType;
	}

	@Override
	public String toString() {
		return "Parameter [name=" + name + ", defaultValue=" + defaultValue
				+ ", description=" + description + ", type=" + type
				+ ", qualifiedType=" + qualifiedType + ", required=" + required
				+ "]";
	}

	public boolean isHeader() {
		return header;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}

	
}
