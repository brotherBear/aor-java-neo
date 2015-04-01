package aor;


public class Parser  {

	
	static String createProvinceInsert(String declaration) {
		String[] parts = declaration.split(";");
		StringBuffer matches = new StringBuffer();
		StringBuffer merges = new StringBuffer();
		merges.append(String.format("MERGE (p:Province {name:'%s', capacity: %s})\n", parts[0].trim(), parts[1].trim()));
		if (parts.length == 3) {
			String[] resources = parts[2].trim().split("-");
			for (int i = 0; i<resources.length;i++) {
				matches.append(String.format("MATCH (r%d:Resource {type: '%s'})\n", i,resources[i]));
				merges.append(String.format("MERGE (p) -[:PROVIDES]-> (r%d)\n",i));
			}
		}
		matches.append(merges);
		
		return matches.toString();
	}

	public static String createResourceInsert(String declaration) {
		String[] parts = declaration.split(" ");
		return String.format("MERGE (:Resource {type:'%s'})", parts[0]);
	}

	public static String createConnection(String declaration) {
		String[] parts = declaration.split(";");
		StringBuffer matches = new StringBuffer();
		matches.append(String.format("MATCH (a:Province {name: '%s'}), (b:Province {name: '%s'})\n", parts[0].trim(), parts[1].trim()));
		StringBuffer merges = new StringBuffer();
		merges.append(String.format("MERGE (a) -[:CONNECT_BY_%s]-> (b)\n",parts[2].trim()));
		merges.append(String.format("MERGE (b) -[:CONNECT_BY_%s]-> (a)\n",parts[2].trim()));
		matches.append(merges);
		return matches.toString();
	}


}
