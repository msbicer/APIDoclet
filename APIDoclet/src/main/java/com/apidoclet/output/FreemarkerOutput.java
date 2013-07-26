package com.apidoclet.output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.apidoclet.model.Result;
import com.itextpdf.text.pdf.BaseFont;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreemarkerOutput {
	private Result result;

	private String fileName;
	
	private String resourceFolder;

	public FreemarkerOutput(String fileName, String resources, Result result) throws Exception {
		this.result = result;
		this.fileName = fileName;
		this.resourceFolder = resources;
	}

	public void write(String template) {
		// parse our markup into an xml Document
		try {
			/* Create and adjust the configuration */
			Configuration cfg = new Configuration();
			cfg.setObjectWrapper(new DefaultObjectWrapper());

			File file = new File(template);
			cfg.setDirectoryForTemplateLoading(file.getParentFile());

			/* Get or create a template */
			Template temp = cfg.getTemplate(file.getName());

			/* Create a data-model */
			Map root = new HashMap();

			root.put("classes", result.getClasses());
			root.put("models", result.getModels());
			root.put("resources", resourceFolder);

			if (fileName.endsWith(".pdf")) {
				/* Merge data-model with template */
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Writer out = new OutputStreamWriter(baos);
				temp.process(root, out);
				out.flush();
				
				DocumentBuilder builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				Document doc = builder.parse(new ByteArrayInputStream(baos
						.toByteArray()));
				ITextRenderer renderer = new ITextRenderer();

				if (resourceFolder!=null){
					File fontsDir = new File(resourceFolder+"fonts/");
					if (fontsDir!=null){
						for (File font : fontsDir.listFiles()) {
							renderer.getFontResolver().addFont(font.getCanonicalPath(),
									BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
						}
					}
				}

				renderer.setDocument(doc, null);
				renderer.layout();
				OutputStream os = new FileOutputStream(fileName);
				renderer.createPDF(os);
				os.close();
			} else {
				Writer out2 = new OutputStreamWriter(new FileOutputStream(
						fileName));
				temp.process(root, out2);
				out2.flush();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
