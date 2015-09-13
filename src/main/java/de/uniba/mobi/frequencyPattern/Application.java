package de.uniba.mobi.frequencyPattern;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import de.uniba.mobi.jdbc.DBConnection;

public class Application {

	private static int index = 0;
	private static int numberOfElements = 0;
	private static LocalTime eventBeginOfDay = LocalTime.of(12, 0);
	private static LocalTime eventEndOfDay = LocalTime.of(18, 0);

	public static void main(String[] args) {
		try {
			DBConnection connection = new DBConnection();
			connection.connect();
			createNodesFile(connection, "nodes_quick.ser");
			connection.disconnect();
			ArrayList<Node> nodes = readNodesFromFile("nodes_quick.ser");
			writeCSVFromNodes(nodes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createNodesFile(DBConnection connection, String filename)
			throws IOException {
		System.out.println("Getting a lot of information from database. "
				+ "This will take a while.");
		List<String> hashmacs = connection.getAllHashMacs(eventBeginOfDay,
				eventEndOfDay);
		System.out.println("finished");

		// ArrayList<String> shortmacs = new ArrayList<String>();
		// for (int i = 0; i < 100; i++) {
		// shortmacs.add(hashmacs.get(i));
		// }

		numberOfElements = hashmacs.size();
		// numberOfElements = shortmacs.size();

		System.out.println("Creating nodes file from " + numberOfElements
				+ " hashmacs: ");
		JsonFileWriter file = new JsonFileWriter(filename);
		for (String each : hashmacs) {
			// for (String each : shortmacs) {
			Node node = new Node(each);
			node.setTimeline(connection.getTimeline(each, eventBeginOfDay,
					eventEndOfDay));
			file.writeDataToFile(node);
			System.out.print(".");
			if (index++ % 75 == 74)
				System.out.print("\n");

		}
		index = 0;
		file.finish();
		System.out.println("finished");
	}

	private static ArrayList<Node> readNodesFromFile(String filename)
			throws IOException, ClassNotFoundException {
		System.out.println("Reading nodes from file: ");
		ArrayList<Node> result = new ArrayList<>();
		JsonFileReader file = new JsonFileReader(filename);
		for (int i = 0; i < numberOfElements; i++) {
			result.add(file.readDataPointFromFile());
		}
		file.finish();
		System.out.println("finished");
		return result;
	}

	private static void writeCSVFromNodes(ArrayList<Node> nodes)
			throws ClassNotFoundException, IOException {
		System.out.println("Calculating values for " + nodes.size()
				+ " values:");
		FrequencyPattern frequencyPattern = new FrequencyPattern(
				eventBeginOfDay, eventEndOfDay);

		String[] values = new String[numberOfElements + 1];
		values[0] = "";

		// header row
		for (int cellIndex = 0; cellIndex < numberOfElements; cellIndex++) {
			values[cellIndex + 1] = nodes.get(cellIndex).getHashmac();
		}
		new WriteLineRunnable("test_quick.csv", values).start();

		// data rows
		for (int rowIndex = 0; rowIndex < numberOfElements; rowIndex++) {
			System.out.print(".");
			if (rowIndex % 75 == 74)
				System.out.print("\n");
			values = new String[numberOfElements + 1];
			// header column
			values[0] = nodes.get(rowIndex).getHashmac();

			// data columns
			for (int cellIndex = 0; cellIndex < numberOfElements; cellIndex++) {
				float value = rowIndex == cellIndex ? 0f : frequencyPattern
						.frequencyGenerator(nodes.get(rowIndex),
								nodes.get(cellIndex));
				values[cellIndex + 1] = String.valueOf(value);
			}
			new WriteLineRunnable("test_quick.csv", values).start();
		}
		System.out.println("finished");
	}
}
