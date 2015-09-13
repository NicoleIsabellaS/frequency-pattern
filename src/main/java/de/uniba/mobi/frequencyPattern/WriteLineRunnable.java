package de.uniba.mobi.frequencyPattern;

import java.io.IOException;

public class WriteLineRunnable implements Runnable {

	Thread t;
	CsvGenerator csv;
	String[] values;

	public WriteLineRunnable(String filename, String[] values)
			throws IOException {
		csv = new CsvGenerator(filename);
		this.values = values;
	}

	@Override
	public void run() {
		try {
			csv.newRow(values);
			csv.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}

}
