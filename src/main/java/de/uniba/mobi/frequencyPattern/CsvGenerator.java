package de.uniba.mobi.frequencyPattern;

import java.io.FileWriter;
import java.io.IOException;

public class CsvGenerator {

	String fileName;
	FileWriter writer;

	public CsvGenerator(String fileName) throws IOException {
		this.fileName = fileName;
		writer = new FileWriter(fileName, true);
	}

	public void addCell(String value) throws IOException {
		writer.append(value).append(';').flush();
	}

	public void newRow(String[] values) throws IOException {
		for (String each : values) {
			addCell(each);
		}
		writer.append('\n').flush();
	}

	public void close() throws IOException {
		writer.close();
	}

}