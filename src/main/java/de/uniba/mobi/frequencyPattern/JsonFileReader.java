package de.uniba.mobi.frequencyPattern;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFileReader {

	private File file;
	private ObjectMapper mapper;
	private JsonParser parser;

	public JsonFileReader(String filename) throws IOException {
		file = new File(filename);
		mapper = new ObjectMapper();
		parser = mapper.getFactory().createParser(file);
		parser.nextToken();
	}

	public Node readDataPointFromFile() throws IOException,
			ClassNotFoundException {
		parser.nextToken();
		return parser.readValueAs(Node.class);
	}

	public void finish() throws IOException {
		parser.close();
	}

}
