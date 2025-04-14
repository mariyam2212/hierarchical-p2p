package com.aos;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/***
 * log manager class.
 * Peer and Server logs
 */
public class LogUtility {

	private String logFile = "p2p-logs.log";
	private BufferedWriter writer = null;
	private final String logLocation = "logs/";

	/***
	 * Constructor which initializes the log file
	 */
	public LogUtility() {
		try {
			// Create a logs folder if it doesn't exist
			File file = new File(logLocation);
			if (!file.exists())
				file.mkdir();
			
			writer = new BufferedWriter(new FileWriter(logLocation + logFile, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * This method is used to write a text to the log file.
	 * @param logText	Text to be appended to the log file.
	 * @return			Returns true if write is successful else returns false
	 */	
	public boolean write(String logText) {
		boolean isWriteSuccess = false;
		try {
			String timeLog = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
			if (writer != null) {
				logText = String.format("%s => %s", timeLog, logText);
				writer.write(logText);
				String newline = System.getProperty("line.separator");
				writer.write(newline);
				isWriteSuccess = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isWriteSuccess;
	}
	
	/***
	 * This method prints the content of the log file.
	 */
	public void print() {
		BufferedReader br = null;
		File file = new File(logFile);
		int charCount = 0;
		
		System.out.println("\nLOG");
		System.out.println("=========================================================================");
		
		try {
			br = new BufferedReader(new FileReader(logLocation + logFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				charCount += line.length();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (charCount == 0) {
			System.out.println("NO LOGS TO PRINT");
		}

		System.out.println("=========================================================================");
	}
	
	/***
	 * closes the file stream so that the log file.
	 */
	public void close() {
		try {
			if (writer != null) {
				String newline = System.getProperty("line.separator");
				writer.write(newline);
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}