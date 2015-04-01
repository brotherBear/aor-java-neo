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

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// Make sure Neo4j Driver is registered
		Class.forName("org.neo4j.jdbc.Driver");

		// Connect
		final String USER = "neo4j";
		final String PASS = "myneo";
		final String DB_URL = "jdbc:neo4j://localhost:7474/";
		Connection con = DriverManager.getConnection(DB_URL, USER, PASS);

		long start = System.currentTimeMillis();
		dropData(con);
		
		addResources(con);
//		checkResources(con);

		addProvinces(con);
//		checkProvinces(con);

		addSattelites(con);
		
		addRelations(con);
		System.out.println(String.format("Completed loading data in %f seconds!", (System.currentTimeMillis()-start)/1000f));
	}

	private static void dropData(Connection con) throws SQLException {
		System.out.println("Dropping data from database: ");
		try (Statement stmt = con.createStatement()) {
			stmt.executeQuery("MATCH (p)-[r2]-() DELETE p,r2");
		}
	}

	private static void addRelations(Connection con) {
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader("src/main/resources/Relations.dsl");
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

	private static void addSattelites(Connection con) {
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader("src/main/resources/Sattelites.dsl");
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
		System.out.println("Checking Provinces in database: ");
		// Querying
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery("MATCH (n:Province) RETURN n.name");
			while (rs.next()) {
				System.out.println(rs.getString("n.name"));
			}
		}
	}

	private static void addProvinces(Connection con)  {
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader("src/main/resources/Provinces.dsl");
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
		System.out.println("Checking Resources in database: ");
		// Querying
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery("MATCH (n:Resource) RETURN n.type");
			while (rs.next()) {
				System.out.println(rs.getString("n.type"));
			}
		}

	}

	private static void addResources(Connection con) throws SQLException {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader("src/main/resources/Resources.dsl");
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
