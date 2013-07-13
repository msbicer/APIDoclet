package com.apidoclet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.apidoclet.builder.ClassBuilder;
import com.apidoclet.model.Class;
import com.apidoclet.model.Method;
import com.apidoclet.model.Parameter;
import com.apidoclet.model.Result;
import com.apidoclet.output.FreemarkerOutput;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;

public class APIDoclet {

	public static final HashMap<String, Class> classMap = new HashMap<String, Class>();

	public static boolean start(RootDoc root) {
		classMap.clear();

		ClassDoc[] classDocs = root.classes();
		List<Class> classes = collectClasses(classDocs);
		HashMap<String, Class> models = collectModels();

		Result result = new Result();
		result.setClasses(classes);
		result.setModels(models);

		String output = readOption("-output", root.options());
		String template = readOption("-template", root.options());
		try {
			new FreemarkerOutput(output, result).write(template);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	private static List<Class> collectClasses(ClassDoc[] classes) {
		List<com.apidoclet.model.Class> klazzes = new ArrayList<com.apidoclet.model.Class>();

		for (int i = 0; i < classes.length; i++) {
			Class klazz = ClassBuilder.build(classes[i]);

			classMap.put(klazz.getQualifiedName(), klazz);
			if (klazz.isController()) {
				klazzes.add(klazz);
			}
		}
		return klazzes;
	}

	private static HashMap<String, Class> collectModels() {
		HashMap<String, Class> models = new HashMap<String, Class>();
		Iterator<String> it = classMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Class klazz = classMap.get(key);
			List<Method> methods = klazz.getHandlers();
			if (methods != null) {
				for (Method method : methods) {
					String response = method.getQualifiedResponseType();
					if (response != null && classMap.containsKey(response)) {
						models.put(response, classMap.get(response));
					}
					List<Parameter> params = method.getParameters();
					if (params != null) {
						for (Parameter param : params) {
							String type = param.getQualifiedType();
							if (classMap.containsKey(type)) {
								models.put(type, classMap.get(type));
							}
						}
					}
				}
			}
		}
		return models;
	}

	private static String readOption(String option, String[][] options) {
		String value = null;
		for (int i = 0; i < options.length; i++) {
			String[] opt = options[i];
			if (opt[0].equals(option)) {
				value = opt[1];
			}
		}
		return value;
	}

	public static int optionLength(String option) {
		if (option.equals("-output") || option.equals("-template")) {
			return 2;
		}
		return 0;
	}

	public static boolean validOptions(String options[][],
			DocErrorReporter reporter) {
		boolean foundTemplateOption = false;
		boolean foundOutputOption = false;
		for (int i = 0; i < options.length; i++) {
			String[] opt = options[i];
			if (opt[0].equals("-output")) {
				foundOutputOption = true;
			} else if (opt[0].equals("-template")) {
				foundTemplateOption = true;
			}
		}
		if (!foundTemplateOption || !foundOutputOption) {
			reporter.printError("Usage: javadoc -output test.pdf -template templates/test.ftl -doclet APIDoclet ...");
		}
		return foundTemplateOption && foundOutputOption;
	}
}
