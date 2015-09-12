package de.uniba.mobi.frequencyPattern;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFileWriter {

	private File file;
	private ObjectMapper mapper;
	private JsonGenerator generator;

	public JsonFileWriter(String filename) throws IOException {
		file = new File(filename);
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(file));
		mapper = new ObjectMapper();
		generator = mapper.getFactory().createGenerator(out, JsonEncoding.UTF8)
				.setCodec(mapper).setPrettyPrinter(new DefaultPrettyPrinter());
		generator.writeStartArray();
	}

	public void writeDataToFile(Node data) throws IOException {
		generator.writeObject(data);
		generator.flush();
	}

	public void finish() throws IOException {
		generator.writeEndArray();
		generator.close();
	}

}
