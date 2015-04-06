package aor;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProvincesTest {

	@Test
	public void testDeclareNorthAmerica() {
		String declaration = "North America; 6; Grain-Cloth-Fur";
		assertEquals("MATCH (r0:Resource {type: 'Grain'})\n" + "MATCH (r1:Resource {type: 'Cloth'})\n"
				+ "MATCH (r2:Resource {type: 'Fur'})\n" + "MERGE (p:Province {name:'North America', capacity: 6})\n"
				+ "MERGE (p) -[:PROVIDES]-> (r0)\n" + "MERGE (p) -[:PROVIDES]-> (r1)\n" + "MERGE (p) -[:PROVIDES]-> (r2)\n",
				Parser.createProvinceInsert(declaration));
	}

	@Test
	public void testDeclareBordeaux() {
		String declaration = "Bordeaux; 4; Timber";
		assertEquals("MATCH (r0:Resource {type: 'Timber'})\nMERGE (p:Province {name:'Bordeaux', capacity: 4})\n"
				+ "MERGE (p) -[:PROVIDES]-> (r0)\n", Parser.createProvinceInsert(declaration));
	}

	@Test
	public void testDeclareParis() {
		String declaration = "Paris; 4;Stone";
		assertEquals("MATCH (r0:Resource {type: 'Stone'})\nMERGE (p:Province {name:'Paris', capacity: 4})\n"
				+ "MERGE (p) -[:PROVIDES]-> (r0)\n", Parser.createProvinceInsert(declaration));
	}

	@Test
	public void testCreateArea() {
		String declaration = "Area I; Novgorod; Riga; Mitau";
		assertEquals("MATCH (p1:Province {name: 'Novgorod'}), (p2:Province {name: 'Riga'}), (p3:Province {name: 'Mitau'})\n"
				+ "MERGE (a:Area {name: 'Area I'})\n" + "MERGE (p1) -[:BELONGS_TO]-> (a)\n" + "MERGE (p2) -[:BELONGS_TO]-> (a)\n"
				+ "MERGE (p3) -[:BELONGS_TO]-> (a)\n", Parser.createArea(declaration));
	}

	@Test
	public void testCreateSea() {
		String declaration = "Greenland Sea; Edinburg; Shetland Islands; Iceland";
		assertEquals("MATCH (p1:Province {name: 'Edinburg'}), (p2:Province {name: 'Shetland Islands'}), "
				+ "(p3:Province {name: 'Iceland'})\n" + "MERGE (s:Sea {name: 'Greenland Sea'})\n" + "MERGE (p1) -[:CONNECTS_TO]-> (s)\n"
				+ "MERGE (p2) -[:CONNECTS_TO]-> (s)\n" + "MERGE (p3) -[:CONNECTS_TO]-> (s)\n", Parser.createSea(declaration));
	}

	@Test
	public void testDeclareTimberResource() {
		String declaration = "Timber";
		assertEquals("MERGE (:Resource {type:'Timber'})", Parser.createResourceInsert(declaration));
	}

	@Test
	public void testCreateSattelite() {
		String declaration = "Kamishin; Sarai;Tana";
		assertEquals("MATCH (p1:Province {name: 'Sarai'}), (p2:Province {name: 'Tana'})\n"
				+ "MERGE (p:Province {name: 'Kamishin', capacity: 1})\n" + "MERGE (p) -[:SUPPORTS]-> (p1)\n"
				+ "MERGE (p) -[:SUPPORTS]-> (p2)\n", Parser.createSatteliteConnection(declaration));
	}

	@Test
	public void testCreateSattelite3() {
		String declaration = "Leon; Basque;Toledo; Lisbon";
		assertEquals("MATCH (p1:Province {name: 'Basque'}), (p2:Province {name: 'Toledo'}), (p3:Province {name: 'Lisbon'})\n"
				+ "MERGE (p:Province {name: 'Leon', capacity: 1})\n" + "MERGE (p) -[:SUPPORTS]-> (p1)\n"
				+ "MERGE (p) -[:SUPPORTS]-> (p2)\n" + "MERGE (p) -[:SUPPORTS]-> (p3)\n", Parser.createSatteliteConnection(declaration));
	}

	@Test
	public void testConnectProvincesByLand() {
		String declaration = "Paris; St.Malo; LAND";
		assertEquals("MATCH (a:Province {name: 'Paris'}), (b:Province {name: 'St.Malo'})\n" + "MERGE (a) -[:CONNECT_BY_LAND]-> (b)\n"
				+ "MERGE (b) -[:CONNECT_BY_LAND]-> (a)\n", Parser.createConnection(declaration));
	}

	@Test
	public void testConnectProvincesByCoast() {
		String declaration = "Paris; London; COAST";
		assertEquals("MATCH (a:Province {name: 'Paris'}), (b:Province {name: 'London'})\n" + "MERGE (a) -[:CONNECT_BY_COAST]-> (b)\n"
				+ "MERGE (b) -[:CONNECT_BY_COAST]-> (a)\n", Parser.createConnection(declaration));
	}

	@Test
	public void testConnectProvincesByLandAndCoast() {
		String declaration = "Paris; London; LAND-COAST";
		assertEquals("MATCH (a:Province {name: 'Paris'}), (b:Province {name: 'London'})\n" + "MERGE (a) -[:CONNECT_BY_LAND]-> (b)\n"
				+ "MERGE (b) -[:CONNECT_BY_LAND]-> (a)\n" + "MERGE (a) -[:CONNECT_BY_COAST]-> (b)\n"
				+ "MERGE (b) -[:CONNECT_BY_COAST]-> (a)\n", Parser.createConnection(declaration));
	}
}
