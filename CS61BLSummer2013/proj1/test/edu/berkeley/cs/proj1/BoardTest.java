package edu.berkeley.cs.proj1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.TestCase;

public class BoardTest extends TestCase {
	
	// Check empty board.
	public void testEmptyBoard ( ) {
		Board b = new Board ( );
		assertTrue (b.isOK ( ));
		checkCollection (b, 0, 0); // applies more tests
		assertTrue (!b.formsTriangle (new Connector (1, 2), Color.RED));
	}
	
	// Check one-connector board.
	public void test1Connector ( ) {
		Board b = new Board ( );
		b.add (new Connector (1, 2), Color.RED);
		assertTrue (b.isOK ( ));
		checkCollection (b, 1, 0);
		
		Iterator<Connector> iter = b.connectors (Color.RED);
		assertTrue (iter.hasNext ( ));
		Connector cnctr = iter.next ( );
		assertEquals (b.colorOf (cnctr), Color.RED);
		assertEquals (new Connector (1, 2), cnctr);
		assertTrue (!iter.hasNext ( ));
		
		assertTrue (!b.formsTriangle (new Connector(1,3), Color.RED));
		assertTrue (!b.formsTriangle (new Connector(5,6), Color.RED));
		assertTrue (!b.choice ( ).equals (new Connector (1, 2)));
		assertEquals (b.colorOf (b.choice ( )), Color.WHITE);
	}
	
	public void testAdd() {
		Board b = new Board ( );
		Connector cnctr1 = new Connector (1, 6);
		b.add (cnctr1, Color.RED); // Tests when input p1 > p2
		assertEquals (b.colorOf(cnctr1), Color.RED);
		Connector cnctr2 = new Connector (1, 5);
		b.add (cnctr2, Color.BLUE);
		assertEquals (b.colorOf(cnctr2), Color.BLUE);
		Connector cnctr3 = new Connector (1, 4);
		assertEquals (b.colorOf(cnctr3), Color.WHITE); // Tests add() doesn't change anything else on board	
		
	}
	
	public void testConnectors() {
		Board b = new Board ( );
		Connector cnctr1 = new Connector (1, 3);
		Connector cnctr2 = new Connector (1, 4);
		Connector cnctr3 = new Connector (1, 5);
		Connector cnctr4 = new Connector (1, 6);
		b.add (cnctr1, Color.RED);
		b.add (cnctr2, Color.RED);
		b.add (cnctr3, Color.RED);
		b.add (cnctr4, Color.BLUE);
		
		Iterator<Connector> iter = b.connectors (Color.RED);
		assertTrue (iter.hasNext ( ));
		Connector cnctriter1 = iter.next ( );
		assertTrue (iter.hasNext ( ));
		Connector cnctriter2 = iter.next ( );
		assertTrue (iter.hasNext ( ));
		Connector cnctriter3 = iter.next ( );
		assertFalse (iter.hasNext ( ));
		assertEquals (cnctriter1, cnctr1);
		assertEquals (cnctriter2, cnctr2);
		assertEquals (cnctriter3, cnctr3);
		assertFalse (b.isOK()); // isOK return false due to numbers of color inconsistency (3 red 1 blue)
		
		Iterator<Connector> iter2 = b.connectors ();
		assertTrue (iter2.hasNext ( ));
		Connector cnctriter4 = iter2.next ( );
		assertEquals (b.colorOf(cnctriter4), Color.WHITE);
		assertTrue (iter2.hasNext ( ));
		iter2.next();
		assertTrue (iter2.hasNext ( ));
		iter2.next();
		assertTrue (iter2.hasNext ( ));
		iter2.next();
		iter2.next();
		iter2.next();
		iter2.next();
		iter2.next();
		iter2.next();
		iter2.next();
		iter2.next();
		iter2.next();
		iter2.next();
		iter2.next();
		assertTrue (iter2.hasNext ( ));
		iter2.next();
		assertFalse (iter2.hasNext ( ));
		assertFalse (b.isOK());
								
	}
	
	public void testColorof() {
		Board b = new Board ( );
		Connector cnctr1 = new Connector (1, 4);
		b.add (cnctr1, Color.RED);
		assertEquals (b.colorOf(cnctr1), Color.RED);
		Connector cnctr2 = new Connector (5, 6);
		b.add (cnctr2, Color.BLUE);
		assertEquals (b.colorOf(cnctr2), Color.BLUE);
		Connector cnctr3 = new Connector (2, 5);
		assertEquals (b.colorOf(cnctr3), Color.WHITE);		
		
	}
	
	public void testFormstriangle() {
		Board b = new Board ( );
		Connector cnctr1 = new Connector (1, 5);
		b.add (cnctr1, Color.RED);
		Connector cnctr2 = new Connector (1, 6);
		b.add (cnctr2, Color.RED);
		Connector cnctr3 = new Connector (6, 5);	
		assertTrue (b.formsTriangle(cnctr3, Color.RED));
		
		Board b2 = new Board ( );
		Connector cnctr4 = new Connector (1, 4);
		b2.add (cnctr4, Color.BLUE);
		Connector cnctr5 = new Connector (1, 3);
		b2.add (cnctr5, Color.BLUE);
		Connector cnctr6 = new Connector (3, 4);	
		assertFalse (b2.formsTriangle(cnctr6, Color.RED));
		assertTrue (b2.formsTriangle(cnctr6, Color.BLUE));
		
		Board b3 = new Board ( );
		Connector cnctr9 = new Connector (1, 3);	
		assertTrue (b3.formsTriangle(cnctr9, Color.WHITE));
		
	}
	
	public void testChoice() {
		Board b = new Board ( );
		Connector cnctr1 = new Connector (1, 4);
		b.add (cnctr1, Color.BLUE);
		Connector cnctr2 = new Connector (1, 3);
		b.add (cnctr2, Color.BLUE);
		assertFalse (b.formsTriangle(b.choice(), Color.BLUE));
		
		Board b2 = new Board ( );
		Connector cnctr3 = new Connector (1, 5);
		b2.add (cnctr3, Color.RED);
		Connector cnctr4 = new Connector (1, 6);
		b2.add (cnctr4, Color.BLUE);
		assertEquals (new Connector (5, 6), b2.choice());
		
		Board b3 = new Board ( );
		Connector cnctr5 = new Connector (1, 4);
		b3.add(cnctr5, Color.RED);
		Connector cnctr6 = new Connector (1, 5);
		b3.add(cnctr6, Color.RED);
		assertFalse (b3.choice().equals(new Connector (4, 5)));	
		
	}
	
	public void testIsOK() {
		// board shouldn't have triangles of red/blue
		Board b = new Board ( );
		Connector cnctr5 = new Connector (2, 4);
		b.add (cnctr5, Color.RED);
		Connector cnctr1 = new Connector (1, 4);
		b.add (cnctr1, Color.BLUE);
		assertTrue (b.isOK());
		Connector cnctr6 = new Connector (6, 3);
		b.add (cnctr6, Color.RED);
		assertTrue (b.isOK());
		Connector cnctr2 = new Connector (1, 3);
		b.add (cnctr2, Color.BLUE);
		assertTrue (b.isOK());
		Connector cnctr7 = new Connector (5, 4);
		b.add (cnctr7, Color.RED);
		assertTrue (b.isOK());
		Connector cnctr3 = new Connector (3, 4);
		b.add (cnctr3, Color.BLUE);
		assertFalse (b.isOK());
		
		// cannot recolor a line
		Board b2 = new Board ( );
		b2.add (cnctr6, Color.RED);
		assertTrue (b2.isOK());
		b2.add (cnctr3, Color.BLUE);
		assertTrue (b2.isOK());
		b2.add (cnctr7, Color.RED);
		assertTrue (b2.isOK());
		Connector cnctr4 = new Connector (5, 4);
		b2.add (cnctr4, Color.BLUE);
		assertFalse (b2.isOK());
		
		Board b3 = new Board ( );
		b3.add (new Connector (1, 6), Color.RED);
		assertTrue(b3.isOK());
		
	}
	// (a useful helper method)
	// Make the following checks on a board that should be legal:
	//	Check connector counts (# reds + # blues + # uncolored should be 15.
	//	Check red vs. blue counts.
	//	Check for duplicate connectors.
	//	Check for a blue/red triangle, which shouldn't exist.
	private void checkCollection (Board b, int redCount, int blueCount) {		
		Iterator<Connector> iter = b.connectors (Color.WHITE);
		int whiteCount= 0;
		int i = 0;
		Connector [] allConnectors = new Connector[15];
		for (int j = 1; j < 7; j++) {
			for (int j2 = j+1; j2 < 7; j2++) {
				allConnectors[i] = new Connector(j,j2);
				i++;
				}		
		}
	
		while (iter.hasNext()) {
			whiteCount++;
			iter.next();
		}
		
		int totalCount = redCount + blueCount + whiteCount;
		assertEquals (totalCount, 15);
		assertTrue (redCount - 1 == blueCount || redCount == blueCount);
		
		Iterator<Connector> blueConnectors = b.connectors(Color.BLUE);
    	while (blueConnectors.hasNext()) {
			Connector blueConnector = blueConnectors.next();
			assertFalse(b.formsTriangle(blueConnector, Color.BLUE));
		}
    	Iterator<Connector> redConnectors = b.connectors(Color.RED);
    	while (redConnectors.hasNext()) {
			Connector redConnector = redConnectors.next();
			assertFalse(b.formsTriangle(redConnector, Color.RED));
		}		
		
		for (int l = 0; l < allConnectors.length; l++) {
		    for (int m = l + 1; m < allConnectors.length; m++) {
		        assertFalse (allConnectors[l].equals(allConnectors[m]));
		        }
		    }
		}
	}