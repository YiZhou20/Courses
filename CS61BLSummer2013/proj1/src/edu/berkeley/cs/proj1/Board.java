package edu.berkeley.cs.proj1;

import java.awt.Color;
import java.util.*;

public class Board {
    
    public static boolean iAmDebugging = true;
    private Color[ ] myColors;
    private Connector[ ] myConnectors;
    private Connector[ ] myColConnectors;

    
    
    // Initialize an empty board with no colored edges.
    public Board ( ) {
        myConnectors = new Connector[15];
        int i = 0;
        for (int j = 1; j < 7; j++) {
            for (int j2 = j+1; j2 < 7; j2++) {
                myConnectors[i] = new Connector(j,j2);
                i++;
                }        
        }
        myColors = new Color[15];
        for (int j = 0; j < 15; j++) {
            myColors[j] = Color.WHITE;
        }
    }
    
    
    // Add the given connector with the given color to the board.
    // Unchecked precondition: the given connector is not already chosen
    // as RED or BLUE.
    public void add (Connector cnctr, Color c) {
    	for (int i = 0; i < 15; i++) {
    		if (cnctr.equals(myConnectors[i])) {
    	    	myColors[i] = c;
    	    	break;
    	    }	
    	}
    }
    
    
    // Set up an iterator through the connectors of the given color, 
    // which is either RED, BLUE, or WHITE. 
    // If the color is WHITE, iterate through the uncolored connectors.
    // No connector should appear twice in the iteration.
    public Iterator<Connector> connectors (Color c) {
    	
        int nColor = 0;
        for (int i = 0; i < 15; i++) {
            if (myColors[i] == c) {
                nColor++;
            }    
        }
        myColConnectors= new Connector[nColor];
        int colorIndex = 0;
        for (int i = 0; i < myConnectors.length; i++) {
            if (myColors[i] == c) {
                myColConnectors[colorIndex] = myConnectors[i];
                colorIndex++;

            }
        }
        Iterator<Connector> myColIterator = new Iterator<Connector> (){
        	int myCount = 0;
        	
        	public boolean hasNext() {
                return myCount < myColConnectors.length;
            }
        	
        	public Connector next() {
                myCount++;
                return myColConnectors[myCount-1];
            }
        	
        	public void remove() {
                
            }
        
        };
        
        return myColIterator;
        
    }
    
    
    // Set up an iterator through all the 15 connectors.
    // No connector should appear twice in the iteration.
    public Iterator<Connector> connectors ( ) {
    	
    	Iterator<Connector> myConIterator = new Iterator<Connector> (){
    		int myCount = 0;

        	public boolean hasNext() {
        		return myCount < 15;
        	}

        	public Connector next() {
        		myCount++;
        		return myConnectors[myCount-1];
        	}

        	public void remove() {
        	}
    	};
    	
    	return myConIterator;
    	
    }
    
    
    // Return the color of the given connector.
    // If the connector is colored, its color will be RED or BLUE;
    // otherwise, its color is WHITE.
    public Color colorOf (Connector e) {
    	Color rtnColor = Color.WHITE;
    	for (int i = 0; i < 15; i++) {
    		if (e.equals(myConnectors[i])) {
    			rtnColor = myColors[i];
    			break;
    		}
    	}
    	
    	return rtnColor;

    }
    
    
    // Unchecked prerequisite: cnctr is an initialized uncolored connector.
    // Let its endpoints be p1 and p2.
    // Return true exactly when there is a point p3 such that p1 is adjacent
    // to p3 and p2 is adjacent to p3 and those connectors have color c.
    public boolean formsTriangle (Connector cnctr, Color c) {
    	boolean formTri = false;
    	int p1 = cnctr.endPt1();
    	int p2 = cnctr.endPt2();
    	int[ ] freePts= new int[4];
    	int posIndex = 0;
    	for (int i = 1; i < 7; i++) {
    		if (i != p1 && i != p2) {
    			freePts[posIndex] = i;
    			posIndex++;
    		}
    	}
    	for (int i = 0; i < 4; i++) {
    		Connector c1 = new Connector(freePts[i],p1);
    		Connector c2 = new Connector(freePts[i],p2);

    		for (int j = 0; j < 15; j++) {
    			if (myConnectors[j].equals(c1) && myColors[j] == c) {
    				for (int j2 = 0; j2 < 15; j2++) {
    					if (myConnectors[j2].equals(c2) && myColors[j2] == c) {
    						formTri = true;
    						break;
    					}
    				}
    				break;
    			}
    		}
    		if (formTri) {
    			break;
    		}
    	}

    	return formTri;

    }
    
    
    // The computer (playing BLUE) wants a move to make.
    // The board is assumed to contain an uncolored connector, with no 
    // monochromatic triangles.
    // There must be an uncolored connector, since if all 15 connectors are colored,
    // there must be a monochromatic triangle.
    // See detailed description of choice tactic in readme file
    public Connector choice ( ) {
    	Connector rtnConnector = new Connector(0,0);
    	Iterator<Connector> nonConnectors = this.connectors(Color.WHITE);
    	while (nonConnectors.hasNext()) {
			Connector nonConnector = nonConnectors.next();
			rtnConnector = nonConnector;
			if (this.formsTriangle(nonConnector, Color.BLUE)) {
				for (int i = 0; i < 15; i++) {
					if (nonConnector.equals(myConnectors[i])) {
						myColors[i] = Color.BLACK;
						break;
					}
				}
			}
    	}
    	nonConnectors = this.connectors(Color.WHITE);
    	while (nonConnectors.hasNext()) {
			Connector nonConnector = nonConnectors.next();
			rtnConnector = nonConnector;
			if (this.formsTriangle(nonConnector, Color.RED)) {
				for (int i = 0; i < 15; i++) {
					if (nonConnector.equals(myConnectors[i])) {
						myColors[i] = Color.BLACK;
						break;
					}
				}
			}
    	}
    	nonConnectors = this.connectors(Color.WHITE);
    	while (nonConnectors.hasNext()) {
    		Connector nonConnector = nonConnectors.next();
			rtnConnector = nonConnector;
			int p1 = nonConnector.endPt1();
	    	int p2 = nonConnector.endPt2();
	    	int[ ] freePts= new int[4];
	    	int posIndex = 0;
	    	for (int i = 1; i < 7; i++) {
	    		if (i != p1 && i != p2) {
	    			freePts[posIndex] = i;
	    			posIndex++;
	    		}
	    	}
			for (int i = 0; i < 4; i++) {
		    	Connector c1 = new Connector(freePts[i],p1);
		    	Connector c2 = new Connector(freePts[i],p2);

		    	for (int j = 0; j < 15; j++) {
		    		if (myConnectors[j].equals(c1) && myColors[j] == Color.BLUE) {
		    			for (int j2 = 0; j2 < 15; j2++) {
		    				if (myConnectors[j2].equals(c2) && (myColors[j2] == Color.WHITE || myColors[j2] == Color.BLACK)) {
		    					for (int k = 0; k < 15; k++) {
									if (nonConnector.equals(myConnectors[k])) {
										myColors[k] = Color.BLACK;
										break;
									}
								}
		    					break;
		    				}
		    			}
		    			break;
		    		} else if (myConnectors[j].equals(c1) && (myColors[j] == Color.WHITE || myColors[j] == Color.BLACK)) {
		    			for (int j2 = 0; j2 < 15; j2++) {
		    				if (myConnectors[j2].equals(c2) && myColors[j2] == Color.BLUE) {
		    					for (int k = 0; k < 15; k++) {
									if (nonConnector.equals(myConnectors[k])) {
										myColors[k] = Color.BLACK;
										break;
									}
								}
		    					break;		    					
		    				}
		    			}
		    			break;
		    		}
		    	}
			}
    	}
    	nonConnectors = this.connectors(Color.WHITE);
    	while (nonConnectors.hasNext()) {
    		Connector nonConnector = nonConnectors.next();
			rtnConnector = nonConnector;
			int p1 = nonConnector.endPt1();
	    	int p2 = nonConnector.endPt2();
	    	int[ ] freePts= new int[4];
	    	int posIndex = 0;
	    	for (int i = 1; i < 7; i++) {
	    		if (i != p1 && i != p2) {
	    			freePts[posIndex] = i;
	    			posIndex++;
	    		}
	    	}
			for (int i = 0; i < 4; i++) {
		    	Connector c1 = new Connector(freePts[i],p1);
		    	Connector c2 = new Connector(freePts[i],p2);

		    	for (int j = 0; j < 15; j++) {
		    		if (myConnectors[j].equals(c1) && myColors[j] == Color.RED) {
		    			for (int j2 = 0; j2 < 15; j2++) {
		    				if (myConnectors[j2].equals(c2) && (myColors[j2] == Color.WHITE || myColors[j2] == Color.BLACK)) {
		    					for (int k = 0; k < 15; k++) {
									if (nonConnector.equals(myConnectors[k])) {
										myColors[k] = Color.BLACK;
										break;
									}
								}
		    					break;
		    				}
		    			}
		    			break;
		    		} else if (myConnectors[j].equals(c1) && (myColors[j] == Color.WHITE || myColors[j] == Color.BLACK)) {
		    			for (int j2 = 0; j2 < 15; j2++) {
		    				if (myConnectors[j2].equals(c2) && myColors[j2] == Color.RED) {
		    					for (int k = 0; k < 15; k++) {
									if (nonConnector.equals(myConnectors[k])) {
										myColors[k] = Color.BLACK;
										break;
									}
								}
		    					break;		    					
		    				}
		    			}
		    			break;
		    		}
		    	}
			}
    	}
    	nonConnectors = this.connectors(Color.WHITE);
    	while (nonConnectors.hasNext()) {
    		Connector nonConnector = nonConnectors.next();
			rtnConnector = nonConnector;
			int p1 = nonConnector.endPt1();
	    	int p2 = nonConnector.endPt2();
	    	int[ ] freePts= new int[4];
	    	int posIndex = 0;
	    	for (int i = 1; i < 7; i++) {
	    		if (i != p1 && i != p2) {
	    			freePts[posIndex] = i;
	    			posIndex++;
	    		}
	    	}
			for (int i = 0; i < 4; i++) {
		    	Connector c1 = new Connector(freePts[i],p1);
		    	Connector c2 = new Connector(freePts[i],p2);

		    	for (int j = 0; j < 15; j++) {
		    		if (myConnectors[j].equals(c1) && (myColors[j] == Color.WHITE || myColors[j] == Color.BLACK)) {
		    			for (int j2 = 0; j2 < 15; j2++) {
		    				if (myConnectors[j2].equals(c2) && (myColors[j2] == Color.WHITE || myColors[j2] == Color.BLACK)) {
		    					for (int k = 0; k < 15; k++) {
									if (nonConnector.equals(myConnectors[k])) {
										myColors[k] = Color.BLACK;
										break;
									}
								}
		    					break;
		    				}
		    			}
		    			break;
		    		}
		    	}
			}
    	}
    	nonConnectors = this.connectors(Color.WHITE);
    	while (nonConnectors.hasNext()) {
    		Connector nonConnector = nonConnectors.next();
    		rtnConnector = nonConnector;
    		break;
    	}
    	for (int i = 0; i < 15; i++) {
			if (myColors[i] == Color.BLACK) {
				myColors[i] = Color.WHITE;
			}
		}

    	return rtnConnector;
    
    }
    
    
    // Return true if the instance variables have correct and internally
    // consistent values.  Return false otherwise.
    // Unchecked prerequisites:
    //    Each connector in the board is properly initialized so that 
    // 1 <= myPoint1 < myPoint2 <= 6.
    public boolean isOK ( ) {
    	boolean allgood = true;
    	
    	
    	// consistent length
    	if (myColors.length != 15 || myConnectors.length != 15) {
    		allgood = false;
    	}

    	// no duplicate items
    	for (int l = 0; l < myConnectors.length; l++) {
    	    for (int m = l + 1; m < myConnectors.length; m++) {
    	        if (myConnectors[l].equals(myConnectors[m])) {
    	        	allgood = false;
    	        	break;
    	        }
    	    }
    	}

    	// no triangle of red/blue color existing
    	Iterator<Connector> blueConnectors = this.connectors(Color.BLUE);
    	while (blueConnectors.hasNext()) {
			Connector blueConnector = blueConnectors.next();
			if (this.formsTriangle(blueConnector, Color.BLUE)) {
				allgood = false;
				break;
			}
		}
    	Iterator<Connector> redConnectors = this.connectors(Color.RED);
    	while (redConnectors.hasNext()) {
			Connector redConnector = redConnectors.next();
			if (this.formsTriangle(redConnector, Color.RED)) {
				allgood = false;
				break;
			}
		}
    	
    	// color consistency
    	int whiteCount = 0;
    	int redCount = 0;
    	int blueCount = 0;

    	for (int n = 0; n < 15; n++) {
    		if (myColors[n] == Color.WHITE) {
    			whiteCount++;		
    		} else 
    			if (myColors[n] == Color.RED) {
    				redCount++;
    			} else
    				if (myColors[n] == Color.BLUE) {
    					blueCount++;
    				}
    	}

    	if (whiteCount + redCount + blueCount != 15 || redCount > blueCount + 1 || redCount < blueCount) {
    		allgood = false;
    	}

    	return allgood;
    	}
    	
}