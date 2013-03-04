package com.apidoclet.model;

import java.util.List;

public class Method implements Element {
	private String name;
	private String responseName;
	private String responseType;
	private String responseDescription;
	private String description;

	private List<String> endpoints;
	private List<String> methods;
	private List<Parameter> parameters;

	private String requestExample;
	private String responseExample;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResponseName() {
		return responseName;
	}

	public void setResponseName(String responseName) {
		this.responseName = responseName;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

	public List<String> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<String> endpoints) {
		this.endpoints = endpoints;
	}

	public String getRequestExample() {
		return requestExample;
	}

	public void setRequestExample(String requestExample) {
		this.requestExample = requestExample;
	}

	public String getResponseExample() {
		return responseExample;
	}

	public void setResponseExample(String responseExample) {
		this.responseExample = responseExample;
	}

	@Override
	public String toString() {
		return "Method [name=" + name + ", responseName=" + responseName
				+ ", responseType=" + responseType + ", responseDescription="
				+ responseDescription + ", description=" + description
				+ ", endpoints=" + endpoints + ", methods=" + methods
				+ ", parameters=" + parameters + ", requestExample="
				+ requestExample + ", responseExample=" + responseExample + "]";
	}
}
