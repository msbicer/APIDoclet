package com.apidoclet;

import java.util.ArrayList;
import java.util.List;

import com.apidoclet.builder.ClassBuilder;
import com.apidoclet.model.Class;
import com.apidoclet.output.FreemarkerOutput;
import com.apidoclet.output.PDFOutput;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;

public class APIDoclet {
	public static boolean start(RootDoc root) {
		List<Class> classes = writeContents(root.classes());
		
		String output = readOption("-output", root.options());
		String template = readOption("-template", root.options());
		try {
			new FreemarkerOutput(output, classes)
					.write(template);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	private static List<Class> writeContents(ClassDoc[] classes) {
		List<com.apidoclet.model.Class> klazzes = new ArrayList<com.apidoclet.model.Class>();
		
		for (int i = 0; i < classes.length; i++) {
			Class klazz = ClassBuilder.build(classes[i]);
			if (klazz!=null){
				klazzes.add(klazz);
			}
		}
		return klazzes;
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
