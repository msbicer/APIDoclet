package com.apidoclet.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apidoclet.model.Class;
import com.apidoclet.model.Method;
import com.apidoclet.model.RequestMapping;
import com.apidoclet.util.BuilderUtils;
import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Tag;

public class MethodBuilder {
	public static Method build(MethodDoc methodDoc, Class klazz) {

		System.out.println("\nMethod to be inspected: " + methodDoc.name()
				+ "\n");

		Method method = new Method();

		// private String responseName; ???
		// private String method;
		// private String endpoint;

		List<com.apidoclet.model.Parameter> parameters = new ArrayList<com.apidoclet.model.Parameter>();
		method.setName(methodDoc.name());
		method.setResponseType(methodDoc.returnType().simpleTypeName());

		// evaluate javadoc tags
		Tag[] tags = methodDoc.tags();
		if (tags.length > 0) {
			for (Tag tag:tags){
				if ("@return".equals(tag.name())){
					method.setResponseDescription(tag.text());
				} else if ("@requestExample".equals(tag.name())){
					method.setRequestExample(tag.text());
				} else if ("@responseExample".equals(tag.name())){
					method.setResponseExample(tag.text());
				} else if ("@name".equals(tag.name())){
					method.setName(tag.text());
				}
			}
		}

		boolean isMapped = false;
		// evaluate annotations
		AnnotationDesc[] annotations = methodDoc.annotations();
		for (AnnotationDesc annotation : annotations) {
			AnnotationTypeDoc type = annotation.annotationType();
			String annotationName = type.qualifiedName();
			if ("org.springframework.web.bind.annotation.RequestMapping"
					.equals(annotationName)) {
				isMapped = true;
				
				RequestMapping mapping = BuilderUtils.resolveRequestMapping(annotation);
				method.setMethods(mapping.getMethod());
				method.setEndpoints(mapping.getValue());
				if (klazz.getEndpoints()!=null){
					List<String> endpoints = new ArrayList<String>();
					for (String kep:klazz.getEndpoints()){
						for (String mep:method.getEndpoints()){
							String ep;
							if (mep.startsWith("/")){
								ep = kep + mep;
							} else {
								ep = kep + "/" + mep;
							}
							endpoints.add(ep);
						}
					}
					method.setEndpoints(endpoints);
				}
				
				for (int i=0,count=method.getEndpoints().size();i<count;i++){
					String mep = method.getEndpoints().get(i);
					if (!mep.startsWith("/")){
						mep = "/" + mep;
						method.getEndpoints().set(i, mep);
					}
				}
				
				if (method.getMethods()==null){
					method.setMethods(Arrays.asList("GET","POST"));
				}
				
			} else if ("org.springframework.web.bind.annotation.ResponseBody"
					.equals(annotationName)) {
				// TODO
			}
//			System.out.println(annotationName);
		}

		if (isMapped) {
			method.setDescription(methodDoc.commentText());
			
			System.out.println(method);

			Parameter[] params = methodDoc.parameters();
			if (params.length > 0) {
				Map<String, ParamTag> map = toMap(methodDoc.tags("param"));

				for (int i = 0; i < params.length; i++) {
					Parameter param = params[i];
					com.apidoclet.model.Parameter parameter = ParameterBuilder
							.build(param, map.get(param.name()));
					if (parameter != null) {
						parameters.add(parameter);
					}
				}
			}

			method.setParameters(parameters);

			return method;
		} else {
			return null;
		}
	}

	private static Map<String, ParamTag> toMap(Tag[] tags) {
		HashMap<String, ParamTag> map = new HashMap<String, ParamTag>();
		for (Tag t : tags) {
			if (t instanceof ParamTag) {
				map.put(((ParamTag) t).parameterName(), (ParamTag) t);
			}

		}
		return map;
	}
}
