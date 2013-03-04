package com.apidoclet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javadoc.JavadocTool;
import com.sun.tools.javadoc.ModifierFilter;

public class APIDocletTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		init(new File("src/test/java/test_project"),"com");
	}

	final private Logger log = Logger.getLogger(APIDocletTest.class.getName());

	private void init(File sourceDirectory, String... packageNames) {

		Context context = new Context();
		Options compOpts = Options.instance(context);

		if (sourceDirectory.exists()) {
			log.debug("Using source path: " + sourceDirectory.getAbsolutePath());
			compOpts.put("-sourcepath", sourceDirectory.getAbsolutePath());
		} else {
			log.info("Ignoring non-existant source path, check your source directory argument "+sourceDirectory.getAbsolutePath());
		}

		ListBuffer<String> subPackages = new ListBuffer<String>();
		for (String packageName : packageNames) {
			log.debug("Adding sub-packages to documentation path: "
					+ packageName);
			subPackages.append(packageName);
		}
		
		new PublicMessager(
                context,
                getApplicationName(),
                new PrintWriter(new LogWriter(Priority.ERROR), true),
                new PrintWriter(new LogWriter(Priority.INFO), true),
                new PrintWriter(new LogWriter(Priority.DEBUG), true)
        );

		JavadocTool javadocTool = JavadocTool.make0(context);
		RootDoc rootDoc = null;
		ListBuffer<String[]> options = new ListBuffer<String[]>();
		options.append(new String[]{"-output", "doclet_test.pdf"});
		options.append(new String[]{"-template", "templates/test.ftl"});
		try {
			
			
			rootDoc = javadocTool.getRootDocImpl("", null, new ModifierFilter(
					ModifierFilter.ALL_ACCESS), new ListBuffer<String>()
					.toList(), options.toList(), false,
					subPackages.toList(), new ListBuffer<String>().toList(),
					false, false, false);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		log.debug("Class Size: "+rootDoc);
		
		if (rootDoc != null) {
			for (ClassDoc classDoc : rootDoc.classes()) {
				log.info("Parsed Javadoc class source: " + classDoc.position()
						+ " with inline tags: " + classDoc.inlineTags().length);
			}
			rootDoc.options();
			APIDoclet.start(rootDoc);
		}
	}
	
	protected class LogWriter extends Writer {
		 
		Priority level;
 
        public LogWriter(Priority level) {
            this.level = level;
        }
 
        public void write(char[] chars, int offset, int length) throws IOException {
//            String s = new String(Arrays.copyOf(chars, length));
//            if (!s.equals("\n"))
//                log.log(level, s);
        }
 
        public void flush() throws IOException {}
        public void close() throws IOException {}
    }

	
	protected String getApplicationName() {
        return getClass().getSimpleName() + " Application";
    }


}
