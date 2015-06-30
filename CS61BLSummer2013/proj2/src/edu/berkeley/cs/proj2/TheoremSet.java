package edu.berkeley.cs.proj2;

import java.util.*;

public class TheoremSet {
	
	private ArrayList<Expression> theorems;
	private ArrayList<String> theoremNames;
	private int theoremCount;

	public TheoremSet ( ) {
		theorems = new ArrayList<Expression>();
		theoremNames = new ArrayList<String> ();
		theoremCount = 0;
	}

	public Expression put (String s, Expression e) {
		theorems.add(theoremCount, e);
		theoremNames.add(theoremCount, s);
		theoremCount++;
		return e;
	}
	
	public ArrayList<String> getNames() {
		return theoremNames;
	}
	
	public ArrayList<Expression> getTheorems() {
		return theorems;
	}
}