package com.apidoclet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.apidoclet.builder.ClassBuilder;
import com.apidoclet.model.Class;
import com.apidoclet.model.Method;
import com.apidoclet.model.Parameter;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javadoc.JavadocTool;
import com.sun.tools.javadoc.ModifierFilter;

public class APIDocletTest {

	private RootDoc rootDoc;

	private String outputPath = "doclet_test.pdf";

	@Before
	public void setUp() throws Exception {
		
		
		init(new File("src/test/java/test_project"), "com");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testOutput(){
		File output = new File(outputPath);
		if (output.exists()){
			output.delete();
		}
		assertFalse(output.exists());
		
		APIDoclet.start(rootDoc);
		
		output = new File(outputPath);
		assertTrue(output.exists());
	}

	@Test
	public void testController() {
		ClassDoc movieController = rootDoc
				.classNamed("com.controller.MovieController");
		assertNotNull(movieController);

		Class klazz = ClassBuilder.build(movieController);
		assertNotNull(klazz);

		assertEquals("MovieController", klazz.getName());
		assertEquals("com.controller.MovieController", klazz.getQualifiedName());
		assertEquals("Movie", klazz.getModule());
		assertTrue(klazz.isController());
		assertFalse(klazz.isWebService());

		assertNotNull(klazz.getMembers());
		assertEquals(0, klazz.getMembers().size());

		assertNotNull(klazz.getEndpoints());
		assertArrayEquals(new String[] { "/movie", "/movie2" }, klazz
				.getEndpoints().toArray());

		assertNull(klazz.getRequestMethods());

		List<Method> handlers = klazz.getHandlers();
		assertNotNull(handlers);
		assertEquals(4, handlers.size());
	}

	@Test
	public void testModelClass() {
		ClassDoc movie = rootDoc.classNamed("com.model.Movie");
		assertNotNull(movie);

		Class klazz = ClassBuilder.build(movie);
		assertNotNull(klazz);

		assertEquals("Movie", klazz.getName());
		assertEquals("com.model.Movie", klazz.getQualifiedName());
		assertNull(klazz.getModule());
		assertFalse(klazz.isController());
		assertFalse(klazz.isWebService());

		assertEquals(2, klazz.getMembers().size());
		assertEquals("name", klazz.getMembers().get(0).getName());
		assertEquals("Name of movie", klazz.getMembers().get(0)
				.getDescription());

		assertEquals("year", klazz.getMembers().get(1).getName());
	}

	@Test
	public void testControllerMethods() {
		ClassDoc movieController = rootDoc
				.classNamed("com.controller.MovieController");
		assertNotNull(movieController);

		Class klazz = ClassBuilder.build(movieController);
		List<Method> handlers = klazz.getHandlers();

		Method method1 = handlers.get(0);
		assertEquals("GetMovie", method1.getName());
		assertEquals("Movie", method1.getResponseType());
		assertEquals("com.model.Movie", method1.getQualifiedResponseType());
		assertEquals("", method1.getDescription());
		assertNull(method1.getRequestExample());
		assertNull(method1.getResponseExample());
		assertEquals("", method1.getResponseDescription());

		assertArrayEquals(new String[] { "/movie/{name}/{param}",
				"/movie2/{name}/{param}" }, method1.getEndpoints().toArray());
		assertArrayEquals(new String[] { "GET" }, method1.getMethods()
				.toArray());

		assertEquals(4, method1.getParameters().size());

		Method method2 = handlers.get(1);
		assertEquals("listMovies", method2.getName());
		assertEquals("String", method2.getResponseType());
		assertEquals("java.lang.String", method2.getQualifiedResponseType());
		assertEquals("Lists movies", method2.getDescription());
		assertEquals("http://example.com/info/id/1234",
				method2.getRequestExample());
		assertEquals(
				"{\n  result: {\n    id: 1737\n    imagePath: http://example.com/images/[size]/Rm/Vy/RmVyZGkgw5Z6YmXEn2Vu.jpg\n    name: \"Info name\"\n  }\n}",
				method2.getResponseExample());
		assertEquals("Test value", method2.getResponseDescription());

		assertArrayEquals(new String[] { "/movie/list1", "/movie/list2",
				"/movie2/list1", "/movie2/list2" }, method2.getEndpoints()
				.toArray());
		assertArrayEquals(new String[] { "GET", "POST" }, method2.getMethods()
				.toArray());

		assertEquals(0, method2.getParameters().size());
	}

	@Test
	public void testMethod1Params() {
		ClassDoc movieController = rootDoc
				.classNamed("com.controller.MovieController");
		assertNotNull(movieController);

		Class klazz = ClassBuilder.build(movieController);

		List<Method> handlers = klazz.getHandlers();

		Method method1 = handlers.get(0);

		Parameter param1_1 = method1.getParameters().get(0);
		assertEquals("name", param1_1.getName());
		assertEquals("Movie", param1_1.getType());
		assertEquals("com.model.Movie", param1_1.getQualifiedType());
		assertNull(param1_1.getDefaultValue());
		assertEquals("Name of movie", param1_1.getDescription());
		assertTrue(param1_1.isRequired());

		Parameter param1_2 = method1.getParameters().get(1);
		assertEquals("param", param1_2.getName());
		assertEquals("Boolean", param1_2.getType());
		assertEquals("java.lang.Boolean", param1_2.getQualifiedType());
		assertNull(param1_2.getDefaultValue());
		assertNull(param1_2.getDescription());
		assertTrue(param1_2.isRequired());

		Parameter param1_3 = method1.getParameters().get(2);
		assertEquals("test4", param1_3.getName());
		assertEquals("String", param1_3.getType());
		assertEquals("java.lang.String", param1_3.getQualifiedType());
		assertNull(param1_3.getDefaultValue());
		assertNull(param1_3.getDescription());
		assertTrue(param1_3.isRequired());

		Parameter param1_4 = method1.getParameters().get(3);
		assertEquals("query", param1_4.getName());
		assertEquals("String", param1_4.getType());
		assertEquals("java.lang.String", param1_4.getQualifiedType());
		assertEquals("test", param1_4.getDefaultValue());
		assertEquals("Query String", param1_4.getDescription());
		assertFalse(param1_4.isRequired());
	}

	final private Logger log = Logger.getLogger(APIDocletTest.class.getName());

	private void init(File sourceDirectory, String... packageNames) {

		Context context = new Context();
		Options compOpts = Options.instance(context);

		if (sourceDirectory.exists()) {
			log.debug("Using source path: " + sourceDirectory.getAbsolutePath());
			compOpts.put("-sourcepath", sourceDirectory.getAbsolutePath());
		} else {
			log.info("Ignoring non-existant source path, check your source directory argument "
					+ sourceDirectory.getAbsolutePath());
		}

		ListBuffer<String> subPackages = new ListBuffer<String>();
		for (String packageName : packageNames) {
			log.debug("Adding sub-packages to documentation path: "
					+ packageName);
			subPackages.append(packageName);
		}

		new PublicMessager(context, getApplicationName(), new PrintWriter(
				new LogWriter(Priority.ERROR), true), new PrintWriter(
				new LogWriter(Priority.INFO), true), new PrintWriter(
				new LogWriter(Priority.DEBUG), true));

		JavadocTool javadocTool = JavadocTool.make0(context);
		ListBuffer<String[]> options = new ListBuffer<String[]>();
		options.append(new String[] { "-output", outputPath });
		options.append(new String[] { "-template", "templates/test.ftl" });
		try {

			rootDoc = javadocTool.getRootDocImpl("", null, new ModifierFilter(
					ModifierFilter.ALL_ACCESS), new ListBuffer<String>()
					.toList(), options.toList(), false, subPackages.toList(),
					new ListBuffer<String>().toList(), false, false, false);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		log.debug("Class Size: " + rootDoc);

		if (rootDoc != null) {
			for (ClassDoc classDoc : rootDoc.classes()) {
				log.info("Parsed Javadoc class source: " + classDoc.position()
						+ " with inline tags: " + classDoc.inlineTags().length);
			}
			rootDoc.options();
			// APIDoclet.start(rootDoc);
		}
	}

	protected class LogWriter extends Writer {

		Priority level;

		public LogWriter(Priority level) {
			this.level = level;
		}

		public void write(char[] chars, int offset, int length)
				throws IOException {
		}

		public void flush() throws IOException {
		}

		public void close() throws IOException {
		}
	}

	protected String getApplicationName() {
		return getClass().getSimpleName() + " Application";
	}

}
