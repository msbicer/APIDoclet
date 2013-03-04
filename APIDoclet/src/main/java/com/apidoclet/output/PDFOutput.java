package com.apidoclet.output;

import java.awt.Color;
import java.io.FileOutputStream;
import java.util.List;

import com.apidoclet.model.Class;
import com.apidoclet.model.Element;
import com.apidoclet.model.Method;
import com.apidoclet.model.Parameter;
import com.apidoclet.util.Utils;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFOutput {

	private List<Class> classes;

	private Document document;
	
    public static final Font TITLE =
        new Font(FontFamily.HELVETICA, 15, Font.BOLD);
    public static final Font BOLD =
            new Font(FontFamily.HELVETICA, 12, Font.BOLD);
    public static final Font NORMAL =
        new Font(FontFamily.HELVETICA, 12);
    public static final Font LINK =
            new Font(FontFamily.HELVETICA, 12, Font.UNDERLINE, new BaseColor(Color.BLUE));

	public PDFOutput(String fileName, List<Class> classes) throws Exception {
		this.classes = classes;

		document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(fileName));
	}

	public void write() {

		try {

			document.open();
			
			renderToc(document);

			//write targets
			for (Class klazz:classes){
				Anchor anchorTarget = new Anchor(Utils.nvl(klazz.getModule(),klazz.getName()), TITLE);
				anchorTarget.setName(klazz.getName());
				
				Paragraph paragraph = new Paragraph();
				paragraph.add(anchorTarget);
				paragraph.setSpacingAfter(30);
				
				document.add(paragraph);
				
				for (Method method:klazz.getHandlers()){
					PdfPTable handler = renderHandler(method);
					handler.setSpacingAfter(30);
					document.add(handler);
//					document.newPage();
				}
			}

			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private void renderToc(Document document) throws DocumentException{
		Paragraph title = new Paragraph("Table of Contents", BOLD);
		title.setSpacingAfter(10);
		
		document.add(title);
		
		//write anchors
		for (Class klazz:classes){
			Anchor anchor = new Anchor(Utils.nvl(klazz.getModule(),klazz.getName()),LINK);
			anchor.setReference("#"+klazz.getName());
			anchor.setFont(LINK);
			
			document.add(anchor);
			
			for (Method method:klazz.getHandlers()){
				Paragraph p = createLink(method);
				p.setIndentationLeft(20);
				document.add(p);
			}
		}
		document.newPage();
	}
	
	private Paragraph createLink(Element element){
		Anchor anchor = new Anchor(element.getName(),LINK);
		anchor.setReference("#"+element.getName());
		anchor.setFont(LINK);
		
		Paragraph paragraph = new Paragraph();
		paragraph.add(anchor);
		return paragraph;
	}
	
	private Paragraph createTarget(Element element){
		Anchor anchorTarget = new Anchor(element.toString(), BOLD);
		anchorTarget.setName(element.getName());
		
		Paragraph paragraph = new Paragraph();
		paragraph.add(anchorTarget);
		return paragraph;
	}
	
	private PdfPTable renderHandler(Method method){
		PdfPTable table = new PdfPTable(new float[] { 1, 1, 2});
        table.setWidthPercentage(100f);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);

        // Add the first header row
        String name = method.getName();
        
		Anchor anchorTarget = new Anchor(name.indexOf("/") < 0 ? name
				: name.substring(name.lastIndexOf("/")), BOLD);
		anchorTarget.setName(method.getName());
        
		table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
		table.setKeepTogether(true);
		
        table.addCell(new Phrase("Function Name", BOLD));
        table.addCell(anchorTarget);
        table.addCell("");
        
        table.addCell(new PdfPCell(new Phrase("Endpoint", NORMAL)));
        table.addCell(new PdfPCell(new Phrase(Utils.toString(method.getEndpoints().toArray(), "\n"), NORMAL) ));
        table.addCell(new PdfPCell());
        
        table.addCell(new PdfPCell(new Phrase("Description", NORMAL)));
        table.addCell(new PdfPCell(new Phrase(method.getDescription(), NORMAL)));
        table.addCell(new PdfPCell());
        
        table.addCell(new PdfPCell(new Phrase("Method", NORMAL)));
        table.addCell(new PdfPCell(new Phrase( Utils.toString(method.getMethods().toArray(), "\n"), NORMAL )));
        table.addCell(new PdfPCell());
        
        if (method.getParameters().size()>0){
	        table.addCell(new Phrase("Request Parameters", BOLD));
	        table.addCell("");
	        table.addCell("");
	        table.addCell(new Phrase("Name", BOLD));
	        table.addCell(new Phrase("Required", BOLD));
	        table.addCell(new Phrase("Description", BOLD));
	        
	        for (Parameter param:method.getParameters()){
	        	table.addCell(new PdfPCell(new Phrase(param.getName()+" : "+param.getType(), NORMAL)));
	            table.addCell(new PdfPCell(new Phrase(param.isRequired()?"Y":"N", NORMAL)));
	            table.addCell(new PdfPCell(new Phrase(param.getDescription(), NORMAL)));
	        }
        }
        table.addCell(new Phrase("Response", BOLD));
        table.addCell("");
        table.addCell("");
        
        table.addCell(new Phrase("Name", BOLD));
        table.addCell(new Phrase("Type", BOLD));
        table.addCell(new Phrase("Description", BOLD));
        
        table.addCell(new PdfPCell(new Phrase(method.getResponseName(), NORMAL)));
        table.addCell(new PdfPCell(new Phrase(method.getResponseType(), NORMAL)));
        table.addCell(new PdfPCell(new Phrase(method.getResponseDescription(), NORMAL)));
        
        table.addCell(new Phrase("Request Example", BOLD));
        PdfPCell requestCell = new PdfPCell(new Phrase(method.getRequestExample(), NORMAL));
        requestCell.setColspan(2);
        table.addCell(requestCell);
        
        table.addCell(new Phrase("Response Example", BOLD));
        PdfPCell responseCell = new PdfPCell(new Phrase(method.getResponseExample(), NORMAL));
        responseCell.setColspan(2);
        table.addCell(responseCell);
        
        return table;
	}
}
