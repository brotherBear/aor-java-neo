package aor;

import static java.lang.String.format;

public class Parser {

	static String createProvinceInsert(String declaration) {
		String[] parts = declaration.split(";");
		StringBuilder matches = new StringBuilder();
		StringBuilder merges = new StringBuilder();
		merges.append("MERGE (p:Province {name:'")
				.append(parts[0].trim())
				.append("', capacity: ")
				.append(parts[1].trim())
				.append("})\n");
		if (parts.length == 3) {
			String[] resources = parts[2].trim().split("-");
			for (int i = 0; i < resources.length; i++) {
				matches.append(format("MATCH (r%d:Resource {type: '%s'})\n", i, resources[i]));
				merges.append(format("MERGE (p) -[:PROVIDES]-> (r%d)\n", i));
			}
		}
		matches.append(merges);

		return matches.toString();
	}

	public static String createResourceInsert(String declaration) {
		String[] parts = declaration.split(" ");
		return format("MERGE (:Resource {type:'%s'})", parts[0]);
	}

	public static String createConnection(String declaration) {
		String[] parts = declaration.split(";");
		StringBuilder matches = new StringBuilder();
		matches.append(format("MATCH (a:Province {name: '%s'}), (b:Province {name: '%s'})\n", parts[0].trim(), parts[1].trim()));
		StringBuilder merges = new StringBuilder();
		if (parts.length == 3) {
			String[] connections = parts[2].trim().split("-");
			for (int i = 0; i < connections.length; i++) {
				merges.append(format("MERGE (a) -[:CONNECT_BY_%s]-> (b)\n", connections[i].trim()));
				merges.append(format("MERGE (b) -[:CONNECT_BY_%s]-> (a)\n", connections[i].trim()));
			}
		}
		matches.append(merges);
		return matches.toString();
	}

	public static String createSatteliteConnection(String declaration) {
		String[] parts = declaration.split(";");
		StringBuilder matches = new StringBuilder("MATCH");
		StringBuilder merges = new StringBuilder();
		if (parts.length > 0) {
			String sat = parts[0].trim();
			for (int i = 1; i < parts.length; i++) {
				matches.append(format(" (p%d:Province {name: '%s'})", i, parts[i].trim()));
				if (i+1 < parts.length) {
					matches.append(",");
				} else {
					matches.append("\n");
				}
				merges.append(format("MERGE (p) -[:SUPPORTS]-> (p%d)\n", i));
			}
			matches.append(format("MERGE (p:Province {name: '%s', capacity: 1})\n", sat));
		}
		matches.append(merges);
		return matches.toString();
	}

	public static String createArea(String declaration) {
		String[] parts = declaration.split(";");
		StringBuilder matches = new StringBuilder("MATCH");
		StringBuilder merges = new StringBuilder();
		if (parts.length > 0) {
			String sat = parts[0].trim();
			for (int i = 1; i < parts.length; i++) {
				matches.append(format(" (p%d:Province {name: '%s'})", i, parts[i].trim()));
				if (i+1 < parts.length) {
					matches.append(",");
				} else {
					matches.append("\n");
				}
				merges.append(format("MERGE (p%d) -[:BELONGS_TO]-> (a)\n", i));
			}
			matches.append(format("MERGE (a:Area {name: '%s'})\n", sat));
		}
		matches.append(merges);
		return matches.toString();
	}

	public static String createSea(String declaration) {
		String[] parts = declaration.split(";");
		StringBuilder matches = new StringBuilder("MATCH");
		StringBuilder merges = new StringBuilder();
		if (parts.length > 0) {
			String sat = parts[0].trim();
			for (int i = 1; i < parts.length; i++) {
				matches.append(format(" (p%d:Province {name: '%s'})", i, parts[i].trim()));
				if (i+1 < parts.length) {
					matches.append(",");
				} else {
					matches.append("\n");
				}
				merges.append(format("MERGE (p%d) -[:CONNECTS_TO]-> (s)\n", i));
			}
			matches.append(format("MERGE (s:Sea {name: '%s'})\n", sat));
		}
		matches.append(merges);
		return matches.toString();
	}

}
