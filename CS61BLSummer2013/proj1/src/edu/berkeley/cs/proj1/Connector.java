package edu.berkeley.cs.proj1;

public class Connector {
	
	// Implement an immutable connector that connects two points on the game board.
	// Invariant: 1 <= myPoint1 < myPoint2 <= 6.
	
	private int myPoint1, myPoint2;
	
	public Connector (int p1, int p2) {
		if (p1 < p2) {
			myPoint1 = p1;
			myPoint2 = p2;
		} else {
			myPoint1 = p2;
			myPoint2 = p1;
		}
	}
	
	public int endPt1 ( ) {
		return myPoint1;
	}
	
	public int endPt2 ( ) {
		return myPoint2;
	}
	
	public boolean equals (Object obj) {
		Connector e = (Connector) obj;
		return (e.myPoint1 == myPoint1 && e.myPoint2 == myPoint2);
	}
	
	public String toString ( ) {
		return "" + myPoint1 + myPoint2;
	}
	
	// Format of a connector is endPoint1 + endPoint2 (+ means string concatenation),
	// possibly surrounded by white space. Each endpoint is a digit between
	// 1 and 6, inclusive; moreover, the endpoints aren't identical.
	// If the contents of the given string is correctly formatted,
	// return the corresponding connector.  Otherwise, throw IllegalFormatException.
	public static Connector toConnector (String s) throws IllegalFormatException {
        s = s.trim();
        int n = 0;
        int m = 0;
        Connector cnctr = new Connector(0,0);
        if (s.length() != 2) {
        	throw new IllegalFormatException("There has to be exactly 2 single integer inputs with no space in between.");
        } else {
        	try {
        		n = Integer.parseInt(s.substring(0, 1));
        	} catch (NumberFormatException e) {
        		throw new IllegalFormatException("First input can only be an integer");
        	}
        	if (n < 1 || n > 6) {
        		throw new IllegalFormatException("Endpioint has to be between 1 and 6");
        	}
        	try {
        		m = Integer.parseInt(s.substring(1, 2));
        	} catch (NumberFormatException e) {
        		throw new IllegalFormatException("Second input can only be an integer");
        	}
        	if (m < 1 || m > 6) {
        		throw new IllegalFormatException("Endpioint has to be between 1 and 6");
        	}
        }
        if (n == m) {
        	throw new IllegalFormatException("Endpoints cannot be identical.");
        } else {
        	cnctr = new Connector(n,m);
        }
        return cnctr;
    }

}