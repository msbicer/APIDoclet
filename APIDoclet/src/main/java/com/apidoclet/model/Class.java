package com.apidoclet.model;

import java.util.List;

public class Class implements Element {
	private String name;
	private String module;
	private String mapping = "";
	private boolean controller;
	private boolean webService;
	
	private List<String> endpoints;
	private List<String> requestMethods;
	private List<Method> handlers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public boolean isController() {
		return controller;
	}

	public void setController(boolean controller) {
		this.controller = controller;
	}

	public boolean isWebService() {
		return webService;
	}

	public void setWebService(boolean webService) {
		this.webService = webService;
	}

	public List<String> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<String> endpoints) {
		this.endpoints = endpoints;
	}

	public List<Method> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<Method> handlers) {
		this.handlers = handlers;
	}

	public List<String> getRequestMethods() {
		return requestMethods;
	}

	public void setRequestMethods(List<String> requestMethods) {
		this.requestMethods = requestMethods;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Override
	public String toString() {
		return "Class [name=" + name + ", module=" + module + ", mapping="
				+ mapping + ", controller=" + controller + ", webService="
				+ webService + ", endpoints=" + endpoints + ", requestMethods="
				+ requestMethods + ", handlers=" + handlers + "]";
	}
}
