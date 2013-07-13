package com.apidoclet.model;

import java.util.HashMap;
import java.util.List;

public class Result {
	private List<Class> classes;
	private HashMap<String, Class> models;
	public List<Class> getClasses() {
		return classes;
	}
	public void setClasses(List<Class> classes) {
		this.classes = classes;
	}
	public HashMap<String, Class> getModels() {
		return models;
	}
	public void setModels(HashMap<String, Class> models) {
		this.models = models;
	}
}
