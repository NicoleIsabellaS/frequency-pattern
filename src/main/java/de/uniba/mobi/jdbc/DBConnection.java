package de.uniba.mobi.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBConnection {

	private Connection connection = null;
	private String url = "jdbc:postgresql://141.13.250.113:5432/bazau_2015_db";
	private String user = "bazau";
	private String password = "bazau_db";

	public void connect() {
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public Map<String, String> getTimeline(String hashmac, LocalTime begin,
			LocalTime end) {
		Map<String, String> output = new HashMap<>();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection
					.prepareStatement("SELECT to_timestamp(cast(epocutc as int)), beamzone FROM bazau2015.observation_wifi WHERE hashmac='"
							+ hashmac
							+ "' AND date_part('day', to_timestamp(cast(epocutc as int))) = "
							+ 18
							+ " AND date_part('hour', to_timestamp(cast(epocutc as int))) >= "
							+ begin.getHour()
							+ " AND date_part('hour', to_timestamp(cast(epocutc as int))) < "
							+ end.getHour() + 1 + " ORDER BY epocutc");
			resultSet = preparedStatement.executeQuery();

			// format: 2015-07-17 22:09:19+02
			while (resultSet.next()) {
				String timestamp = resultSet.getString(1);
				String area = resultSet.getString(2);
				output.put(timestamp, area);
			}

		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}

		return output;
	}

	public List<String> getAllHashMacs(LocalTime begin, LocalTime end) {
		List<String> output = new ArrayList<>();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection
					.prepareStatement("SELECT DISTINCT hashmac FROM bazau2015.observation_wifi "
							+ "WHERE date_part('day', to_timestamp(cast(epocutc as int))) = "
							+ 18
							+ " AND date_part('hour', to_timestamp(cast(epocutc as int))) >= "
							+ begin.getHour()
							+ " AND date_part('hour', to_timestamp(cast(epocutc as int))) < "
							+ end.getHour() + 1 + " ORDER BY hashmac");
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				output.add(resultSet.getString(1));
			}

		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}

		return output;
	}

	public void disconnect() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
