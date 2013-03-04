package com.apidoclet.output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.apidoclet.model.Class;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreemarkerOutput {
	private List<Class> classes;

	private String fileName;

	public FreemarkerOutput(String fileName, List<Class> classes)
			throws Exception {
		this.classes = classes;
		this.fileName = fileName;
		
	}

	public void write(String template) {
		// parse our markup into an xml Document
		try {
			 /* Create and adjust the configuration */
	        Configuration cfg = new Configuration();
	        cfg.setObjectWrapper(new DefaultObjectWrapper());
//	        cfg.setTemplateLoader(new FileSystemTemplateLoader());
	        
	        File file = new File(template);
	        cfg.setDirectoryForTemplateLoading(file.getParentFile());
	        /* ------------------------------------------------------------------- */    
	        /* You usually do these for many times in the application life-cycle:  */    
	                
	        /* Get or create a template */
	        Template temp = cfg.getTemplate(file.getName());

	        /* Create a data-model */
	        Map root = new HashMap();
	        
	        root.put("classes", classes);

	        /* Merge data-model with template */
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        Writer out = new OutputStreamWriter(baos);
	        temp.process(root, out);
	        out.flush();
			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(baos.toByteArray()));
			ITextRenderer renderer = new ITextRenderer();
			
//			
			System.out.println("==> "+FreemarkerOutput.class.getClassLoader().getResource("resources/fonts/").getFile());
			File dir = new File(FreemarkerOutput.class.getClassLoader().getResource("resources/fonts/").getFile());
			System.out.println("1 ==> "+dir);
			System.out.println("2 ==> "+dir.listFiles());
			for (File font:dir.listFiles()){
				renderer.getFontResolver().addFont(
						font.getCanonicalPath(), BaseFont.IDENTITY_H,
						BaseFont.NOT_EMBEDDED);
			}
//			renderer.getFontResolver().addFontDirectory("resources/fonts", BaseFont.NOT_EMBEDDED);
			
			renderer.setDocument(doc, null);
			renderer.layout();
			OutputStream os = new FileOutputStream(fileName);
			renderer.createPDF(os);
			os.close();
			
			Writer out2 = new OutputStreamWriter(new FileOutputStream("test.html"));
	        temp.process(root, out2);
	        out2.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
