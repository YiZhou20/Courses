package edu.berkeley.cs.proj2;

public class LineNumber {
	
	private Integer myLineNumber;
	private String mySubLineNumber;
	
	public LineNumber (Integer num) {
		myLineNumber = new Integer(num);	
	}
	
	public LineNumber (String num) {
		mySubLineNumber = new String (num);
	}
	public Object getLineNumber () {
		if (myLineNumber != null) {
			return myLineNumber;
		} else
			return mySubLineNumber;
		}
	

}
