package edu.berkeley.cs.proj1;

import junit.framework.TestCase;

public class ConnectorTest extends TestCase {
	public void testLegalConnector() {
		Connector c1 = new Connector(1,2);
		Connector cMatch1 = Connector.toConnector("12");
		assertTrue(c1.equals(cMatch1));
		assertFalse(cMatch1.equals(new Connector(2,3)));
		Connector cMatch2 = Connector.toConnector("21");
		assertTrue(c1.equals(cMatch2));
		Connector cMatch3 = Connector.toConnector("   12  ");
		assertTrue(c1.equals(cMatch3));
	}
	
	public void testWrongLengthInput() {
		// should out put "exactly 2 single integer inputs with no space in between" message three times
		Connector c = new Connector(1,2);
		try {
			Connector.toConnector("456");
		} catch (IllegalFormatException e) {
			System.out.println (e.getMessage ( ));
		}
		assertEquals(new Connector(1,2),c);
		try {
			Connector.toConnector("4 5");
		} catch (IllegalFormatException e) {
			System.out.println (e.getMessage ( ));
		}
		assertEquals(new Connector(1,2),c);
		try {
			Connector.toConnector(" 5 ");
		} catch (IllegalFormatException e) {
			System.out.println (e.getMessage ( ));
		}
		assertEquals(new Connector(1,2),c);
	}
	
	public void testNonintegerInput() {
		Connector c = new Connector(1,2);
		try {
			Connector.toConnector("  a6");
		} catch (IllegalFormatException e) {
			System.out.println (e.getMessage ( )); // first input can only be integer
		}
		assertEquals(new Connector(1,2),c);
		try {
			Connector.toConnector(" 6#  ");
		} catch (IllegalFormatException e) {
			System.out.println (e.getMessage ( )); // second input can only be integer
		}
		assertEquals(new Connector(1,2),c);
	}
	
	public void testOutOfBoundaryInput() {
		Connector c = new Connector(1,2);
		try {
			Connector.toConnector("   76");
		} catch (IllegalFormatException e) {
			System.out.println (e.getMessage ( )); // endpoint between 1 and 6
		}
		assertEquals(new Connector(1,2),c);
		try {
			Connector.toConnector("   48  ");
		} catch (IllegalFormatException e) {
			System.out.println (e.getMessage ( )); // endpoint between 1 and 6
		}
		assertEquals(new Connector(1,2),c);
	}
	
	public void testIdenticalEndpoints() {
		Connector c = new Connector(1,2);
		try {
			Connector.toConnector("   55  ");
		} catch (IllegalFormatException e) {
			System.out.println (e.getMessage ( )); // endpoints cannot be identical
		}
		assertEquals(new Connector(1,2),c);
	}

}
