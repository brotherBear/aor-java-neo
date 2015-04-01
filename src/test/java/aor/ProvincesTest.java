package aor;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProvincesTest {

	@Test
	public void testDeclareNorthAmerica() {
		String declaration = "North America; 6; Grain-Cloth-Fur";
		assertEquals("MATCH (r0:Resource {type: 'Grain'})\n"
				+ "MATCH (r1:Resource {type: 'Cloth'})\n"
				+ "MATCH (r2:Resource {type: 'Fur'})\n"
				+ "MERGE (p:Province {name:'North America', capacity: 6})\n"
				+ "MERGE (p) -[:PROVIDES]-> (r0)\n"
				+ "MERGE (p) -[:PROVIDES]-> (r1)\n"
				+ "MERGE (p) -[:PROVIDES]-> (r2)\n", Parser.createProvinceInsert(declaration));
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
	public void testDeclareTimberResource() {
		String declaration = "Timber";
		assertEquals("MERGE (:Resource {type:'Timber'})", Parser.createResourceInsert(declaration));
	}

}
