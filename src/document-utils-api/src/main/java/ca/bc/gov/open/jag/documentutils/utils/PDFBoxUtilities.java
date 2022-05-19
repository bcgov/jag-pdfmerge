/**
 * @(#)PDFBoxUtilities.java
 * Copyright (c) 2012, B.C. Ministry of Attorney General.
 * All rights reserved.
 */
package ca.bc.gov.open.jag.documentutils.utils;

import ca.bc.gov.open.jag.documentutils.adobe.models.MergeDoc;
import ca.bc.gov.open.jag.documentutils.exception.MergeException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.interactive.form.PDXFAResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 
 * PDF Box Utilities 
 * 
 * @author shaunmillargov
 *
 */
public class PDFBoxUtilities {

	private final static Logger logger = LoggerFactory.getLogger(PDFBoxUtilities.class);	

	/**
	 * isPDFXfa() - returns boolean indicating if document is XFA (Smartform) type. 
	 * 
	 * @param pdfFile
	 * @return
	 */
	public static boolean isPDFXfa(byte[] pdfFile) {
		
		boolean isXFA = false;
		PDDocument doc = null;
		PDXFAResource xfa = null;
		try {
			doc =  getPDDocFromBytes(pdfFile);
			if ( null != doc.getDocumentCatalog().getAcroForm()) {
				xfa = doc.getDocumentCatalog().getAcroForm().getXFA();
			} 
			return null != xfa;
		} catch (MergeException | NullPointerException e) {
			logger.error(e.getMessage());
		} finally {			
			try {
				doc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return isXFA;

	}
	
	/**
	 * 
	 * getPDDocFromBytes() - loads a PDDocument from a byte array. 
	 * 
	 * @param pdfFile
	 * @return
	 * @throws MergeException
	 */
	private static PDDocument getPDDocFromBytes(byte[] pdfFile) {
		
		ByteArrayInputStream bis = new ByteArrayInputStream(pdfFile);
			
		try {
			return PDDocument.load(bis);
		} catch (InvalidPasswordException e) {
			throw new MergeException("Password Protected PDF.", e.getCause());
		} catch (IOException e) {
			throw new MergeException("File not a PDF.", e.getCause());
		}
		
	}
	
	/**
	 * Get number of pages of the PDF binary
	 * 
	 * @param file
	 * @return
	 */
	public static int getPages(byte[] file)  {
		PDDocument doc;
		int pages = 0;
		try {
			doc = PDDocument.load(file);
			pages = doc.getNumberOfPages();
			doc.close();
			return pages;
		} catch (IOException e) {
			logger.error("Error caught at getPages: " + e.getMessage() + ". Returning 0 pages");
			e.printStackTrace();
			return pages;
		}
	}
	
	/**
	 * Add a blank page to the rear of the document.
	 * 
	 * @param pdfDoc
	 * @throws IOException 
	 */
	public static void addBlankPage(MergeDoc pdfDoc) throws IOException {
		
		logger.info("Calling addPage");
		
		PDDocument doc = PDDocument.load(pdfDoc.getFile());
		doc.addPage(new PDPage());

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		doc.save(byteArrayOutputStream);
		doc.close();
		
		pdfDoc.setFile(byteArrayOutputStream.toByteArray());
	}

}
