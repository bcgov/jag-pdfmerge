package ca.bc.gov.open.pssg.docmerge.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

/**
 * Base64 encoder/decoder utils based on Apache Commons Codec.
 * 
 * @author shaunmillargov
 *
 */
public class Base64Utils {

	public static void main(String args[]) throws Exception {

		//String sourceFile = "C:\\Users\\176899\\github_projects\\DocumentMerge\\test_files\\pdfsample1.pdf";
		//String sourceFile = "C:\\Users\\176899\\github_projects\\DocumentMerge\\test_files\\pdfsample2.pdf";
		//String sourceFile = "C:\\Users\\176899\\github_projects\\DocumentMerge\\test_files\\RecordOfProceedings_1.5.pdf";
		//String targetFile = "C:\\Users\\176899\\github_projects\\DocumentMerge\\test_files\\pdfsample1.dat";
		//String targetFile = "C:\\Users\\176899\\github_projects\\DocumentMerge\\test_files\\pdfsample2.dat";
		//String targetFile = "C:\\Users\\176899\\github_projects\\DocumentMerge\\test_files\\RecordOfProceedings_1.5.dat";
		//String sourceFile = "C:\\Users\\176899\\github_projects\\DocumentMerge\\test_files\\outtest1.dat";
		String sourceFile = "C:\\Users\\176899\\github_projects\\DocumentMerge\\test_files\\outtest2.dat";
		//String decodedFile = "C:\\Users\\176899\\github_projects\\DocumentMerge\\test_files\\outtest1.pdf";
		String decodedFile = "C:\\Users\\176899\\github_projects\\DocumentMerge\\test_files\\outtest2.pdf";

		//encode(sourceFile, targetFile, false);
		
		decode(sourceFile, decodedFile);
		
	}

	/**
	 * 
	 * Base64 encoding from file to file.
	 * 
	 * IsChunked sets hard wrap on file contents.
	 * 
	 * @param sourceFile
	 * @param targetFile
	 * @param isChunked
	 * @throws Exception
	 */
	public static void encode(String sourceFile, String targetFile, boolean isChunked) throws Exception {

		byte[] base64EncodedData = Base64.encodeBase64(loadFileAsBytesArray(sourceFile), isChunked);

		writeByteArraysToFile(targetFile, base64EncodedData);
	}
	
	
	/**
	 * 
	 * Encode to base64
	 * 
	 * @param input
	 * @param isChunked
	 * @return
	 */
	public static byte[] encode(byte[] input, boolean isChunked) {

		return Base64.encodeBase64(input, isChunked);
	}

	/**
	 * 
	 * Base64 decoding from file to file.
	 * 
	 * @param sourceFile
	 * @param targetFile
	 * @throws Exception
	 */
	public static void decode(String sourceFile, String targetFile) throws Exception {

		byte[] decodedBytes = Base64.decodeBase64(loadFileAsBytesArray(sourceFile));

		writeByteArraysToFile(targetFile, decodedBytes);
	}
	
	/**
	 * 
	 * Decode from base64
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] decode(byte[] input) {

		return Base64.decodeBase64(input);
	}

	/**
	 * This method loads a file from file system and returns the byte array of the
	 * content.
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static byte[] loadFileAsBytesArray(String fileName) throws Exception {

		File file = new File(fileName);
		int length = (int) file.length();
		BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
		byte[] bytes = new byte[length];
		reader.read(bytes, 0, length);
		reader.close();
		return bytes;

	}

	/**
	 * This method writes byte array content into a file.
	 * 
	 * @param fileName
	 * @param content
	 * @throws IOException
	 */
	public static void writeByteArraysToFile(String fileName, byte[] content) throws IOException {

		File file = new File(fileName);
		BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
		writer.write(content);
		writer.flush();
		writer.close();

	}
}
