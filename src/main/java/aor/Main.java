package aor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static Logger log = LoggerFactory.getLogger(Main.class);
	private static final String SRC_MAIN_RESOURCES_DATA = "data/";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// Configure logging
		PropertyConfigurator.configure("log4j.properties");
		// Make sure Neo4j Driver is registered
		log.info("Registering driver for database");
		Class.forName("org.neo4j.jdbc.Driver");

		// Connect
		final String USER = "neo4j";
		final String PASS = "myneo";
		final String DB_URL = "jdbc:neo4j://localhost:7474/";
		Connection con = DriverManager.getConnection(DB_URL, USER, PASS);

		long start = System.currentTimeMillis();
		dropData(con);

		log.info("Reading data from '" + SRC_MAIN_RESOURCES_DATA + "' folder");
		addResources(con);
		// checkResources(con);

		addProvinces(con);
		// checkProvinces(con);

		addSatellites(con);

		addAreas(con);

		addSeas(con);

		addRelations(con);
		log.info(String.format("Completed loading data in %f seconds!", (System.currentTimeMillis() - start) / 1000f));
	}

	private static void addSeas(Connection con) {
		log.info("Adding Seas to database");
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(SRC_MAIN_RESOURCES_DATA + "Seas.dsl");
			br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				Statement stmt = con.createStatement();
				stmt.executeUpdate(Parser.createSea(line));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void addAreas(Connection con) {
		log.info("Adding Areas to database");
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(SRC_MAIN_RESOURCES_DATA + "Areas.dsl");
			br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				Statement stmt = con.createStatement();
				stmt.executeUpdate(Parser.createArea(line));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static void dropData(Connection con) throws SQLException {
		log.info("Dropping data from database: ");
		try (Statement stmt = con.createStatement()) {
			stmt.executeQuery("MATCH (p)-[r2]-() DELETE p,r2");
		}
	}

	private static void addRelations(Connection con) {
		log.info("Adding Relations between Provinces to database");
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(SRC_MAIN_RESOURCES_DATA + "Relations.dsl");
			br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				Statement stmt = con.createStatement();
				stmt.executeUpdate(Parser.createConnection(line));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static void addSatellites(Connection con) {
		log.info("Adding Satellites to database");
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(SRC_MAIN_RESOURCES_DATA + "Sattelites.dsl");
			br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				Statement stmt = con.createStatement();
				stmt.executeUpdate(Parser.createSatteliteConnection(line));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static void checkProvinces(Connection con) throws SQLException {
		log.info("Checking Provinces in database: ");
		// Querying
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery("MATCH (n:Province) RETURN n.name");
			while (rs.next()) {
				System.out.println(rs.getString("n.name"));
			}
		}
	}

	private static void addProvinces(Connection con) {
		log.info("Adding provinces to database");
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(SRC_MAIN_RESOURCES_DATA + "Provinces.dsl");
			br = new BufferedReader(fr);
			while (br.ready()) {
				String line = br.readLine();
				Statement stmt = con.createStatement();
				stmt.executeUpdate(Parser.createProvinceInsert(line));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static void checkResources(Connection con) throws SQLException {
		log.info("Checking Resources in database: ");
		// Querying
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery("MATCH (n:Resource) RETURN n.type");
			while (rs.next()) {
				System.out.println(rs.getString("n.type"));
			}
		}

	}

	private static void addResources(Connection con) throws SQLException {
		log.info("Adding Resources to database");
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(SRC_MAIN_RESOURCES_DATA + "Resources.dsl");
			br = new BufferedReader(fr);
			Stream<String> content = br.lines();
			content.forEach(p -> sb.append(Parser.createResourceInsert(p) + "\n"));
			try (Statement stmt = con.createStatement()) {
				stmt.executeUpdate(sb.toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
