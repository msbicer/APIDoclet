package com.apidoclet.builder;

import java.util.ArrayList;
import java.util.List;

import com.apidoclet.model.Class;
import com.apidoclet.model.Method;
import com.apidoclet.model.Parameter;
import com.apidoclet.model.RequestMapping;
import com.apidoclet.util.BuilderUtils;
import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Tag;

public class ClassBuilder {
	public static Class build(ClassDoc classDoc) {
		Class klazz = new Class();

		klazz.setName(classDoc.name());
		klazz.setDescription(classDoc.commentText());
		klazz.setQualifiedName(classDoc.qualifiedName());
		klazz.setApi(false);
		klazz.setWebService(false);
		klazz.setEndpoints(new ArrayList<String>());
		
		//collect members
		FieldDoc[] fields = classDoc.fields();
		List<Parameter> params = new ArrayList<Parameter>();
		for (FieldDoc field:fields){
			Parameter param = ParameterBuilder.build(field);
			params.add(param);
		}
		klazz.setMembers(params);

		// evaluate javadoc tags
		Tag[] tags = classDoc.tags();
		if (tags.length > 0) {
			for (Tag tag : tags) {
				if ("@module".equals(tag.name())) {
					klazz.setModule(tag.text());
				} else if ("@ignore".equals(tag.name())){
					return null;
				}
			}
		}

		//evaluate annotations
		AnnotationDesc[] annotations = classDoc.annotations();
		for (AnnotationDesc annotation : annotations) {
			AnnotationTypeDoc type = annotation.annotationType();
			String annotationName = type.qualifiedName();
			if ("org.springframework.stereotype.Controller"
					.equals(annotationName)) {
				klazz.setApi(true);
			} else if ("org.springframework.web.bind.annotation.RequestMapping"
					.equals(annotationName)) {
				RequestMapping mapping = BuilderUtils
						.resolveRequestMapping(annotation);

				klazz.setEndpoints(mapping.getValue());
				klazz.setRequestMethods(mapping.getMethod());
			}
		}

		if (klazz.isApi()) {
			List<Method> methodList = new ArrayList<Method>();
			MethodDoc[] methods = classDoc.methods();
			for (int i = 0; i < methods.length; i++) {
				Method method = MethodBuilder.build(methods[i], klazz);
				if (method != null) {
					if (klazz.getRequestMethods() != null) {
						method.setMethods(klazz.getRequestMethods());
					}

					if (method != null) {
						methodList.add(method);
					}
				}
			}
			klazz.setHandlers(methodList);
		}
		return klazz;
	}
}
