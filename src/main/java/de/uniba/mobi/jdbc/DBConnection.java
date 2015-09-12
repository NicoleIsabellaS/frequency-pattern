package de.uniba.mobi.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.uniba.mobi.frequencyPattern.Area;
import de.uniba.mobi.frequencyPattern.TimeAreaPair;

public class DBConnection {

	private static Connection connection = null;
	private static String url = "jdbc:postgresql://141.13.250.113:5432/bazau_2015_db";
	private static String user = "bazau";
	private static String password = "bazau_db";

	public static void connect() {
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public static List<TimeAreaPair> getTimeline(String hashmac) {
		List<TimeAreaPair> output = new ArrayList<>();

		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection
					.prepareStatement("SELECT to_timestamp(cast(epocutc as int)), beamzone FROM bazau2015.observation_wifi WHERE hashmac='"
							+ hashmac
							+ "' AND date_part('day', to_timestamp(cast(epocutc as int))) = 17 AND date_part('hour', to_timestamp(cast(epocutc as int))) >= 19 ORDER BY epocutc"); // TODO
			rs = pst.executeQuery();

			while (rs.next()) {
				output.add(new TimeAreaPair(rs.getString(1), new Area(rs
						.getString(2))));
			}

		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}

		return output;
	}

	public static List<String> getAllHashMacs() {
		List<String> output = new ArrayList<>();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = connection
					.prepareStatement("SELECT DISTINCT hashmac FROM bazau2015.observation_wifi");
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

	public static void disconnect() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
