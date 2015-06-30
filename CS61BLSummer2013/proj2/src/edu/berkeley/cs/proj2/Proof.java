package edu.berkeley.cs.proj2;

import java.util.*;

public class Proof {

	private TheoremSet theoremSet;
	private Integer nextNum = 1;
	private String nextSubNum = "2";
	private ArrayList<String> subproofs = new ArrayList<String>(); // keeps track of each statement that needs to be proved in subproofs
	private int matchproof = -1; // keeps track of which proof to check in subproofs
	private int index = 1;
	private boolean inSubproof = false;
	private String myProofSoFar = "";
	private String myLatestLine = "";
	private String myOutermostExp = "";
	private ArrayList<String> prevlinenos = new ArrayList<String>();
	private ArrayList<String> prevexpr = new ArrayList<String>();
	private boolean onNum = true;
	private boolean afterShow = true;
	private ArrayList<String> myAssumptions = new ArrayList<String>();
	private String lineNum = new String();
	private String lineNum1 = new String();
	private String lineNum2 = new String();
	private String subX2 = new String();

	public Proof(TheoremSet theorems) {
		theoremSet = theorems;
	}

	public Object nextLineNumber() {
		if (!inSubproof) {
			LineNumber nextLineNum = new LineNumber(nextNum);
			return nextLineNum.getLineNumber();
		} else {
			LineNumber nextLineNum = new LineNumber(nextSubNum);
			onNum = false;
			return nextLineNum.getLineNumber();
		}
	}

	public void extendProof(String x) throws IllegalLineException,
			IllegalInferenceException {			
		if (myProofSoFar.isEmpty()) {
			if (!x.contains("show") || x == null || x.isEmpty()) {
				throw new IllegalLineException("First line supplied has to be a 'show' statement");
			}
		}
		if (x == null || x.isEmpty()) {
			throw new IllegalLineException("Must enter a valid line to Proof");
		}
		if (x.contains("print")) { // print
			if (!x.equals("print")) {
				throw new IllegalLineException("Print has to appear alone on the line");
			}
			afterShow = false;
			print();
			return;
		} else if (x.length() > 6 && x.substring(0,6).equals("repeat")) { // repeat
			if (x.length() < 10) {
				throw new IllegalLineException("Input is too short: repeat is followed by blank space, a LineNumber, then blank space, then an expression");
			} else if (x.charAt(6) != ' ') {
				throw new IllegalLineException("Repeat has to be followed by blank space");
			} else {
				String subX = x.substring(7).trim();
				int spacePos = checkOneLineSpace(subX);
				Expression expr = new Expression(subX.substring(spacePos).trim());
				if (!expr.equals(prevexpr.get(prevlinenos.indexOf(lineNum)))) {
					throw new IllegalLineException("Expressions doesn't match");
				}
				afterShow = false;
			}
		} else if (x.length() > 2 && x.substring(0,2).equals("ic")) { // ic
				if (x.length() < 6) {
					throw new IllegalLineException("Input is too short: ic is followed by blank space, a LineNumber, then blank space, then an expression");
				} else if (x.charAt(2) != ' ') {
					throw new IllegalLineException("ic has to be followed by blank space");
				} else {
					String subX = x.substring(3).trim();
					int spacePos = checkOneLineSpace(subX);
					Expression expr = icPattern(subX, spacePos);
				    afterShow = false;
				    myAssumptions.add(expr.getExpr());
				}
		} else if (x.length() > 4 && x.substring(0,4).equals("show")) { // show
			if (x.length() < 6) {
				throw new IllegalLineException("Input is too short: show is followed by blank space, then an expression");
			} else if (x.charAt(4) != ' ') {
				throw new IllegalLineException("Show has to be followed by blank space");
			} else {
				new Expression(x.substring(5).trim());
				afterShow = true;
			}
		} else if (x.length() > 6 && x.substring(0,6).equals("assume")) { // assume
			if (!afterShow) {
				throw new IllegalLineException("Assume must always be preceded by a show statement");
			}		
			if (x.length() < 8) {
				throw new IllegalLineException("Input is too short: assume is followed by blank space, then an expression");
			} else if (x.charAt(6) != ' ') {
				throw new IllegalLineException("Assume has to be followed by blank space");
			} else {
				Expression expr = new Expression(x.substring(7).trim());
				assumePattern(expr);
				afterShow = false;
				myAssumptions.add(expr.getExpr());
			}
		} else if (x.length() > 2 && x.substring(0,2).equals("co")) { // co
			if (x.length() < 8) {
				throw new IllegalLineException("Input is too short: co is followed by blank space, a LineNumber, then blank space, another LineNumber, blank space, then an expression");
			} else if (x.charAt(2) != ' ') {
				throw new IllegalLineException("co has to be followed by blank space");
			} else {
				int spacePos2 = checkTwoLineSpace(x);
				Expression expr = coPattern(subX2, spacePos2);
				afterShow = false;
				myAssumptions.add(expr.getExpr());
			}		
		} else if (x.length() > 2 && x.substring(0,2).equals("mp")) { // mp
			if (x.length() < 8) {
				throw new IllegalLineException("Input is too short: mp is followed by blank space, a LineNumber, then blank space, another LineNumber, blank space, then an expression");
			} else if (x.charAt(2) != ' ') {
				throw new IllegalLineException("mp has to be followed by blank space");
			} else {
				int spacePos2 = checkTwoLineSpace(x);
				Expression expr = mpPattern(subX2, spacePos2);
				afterShow = false;
				myAssumptions.add(expr.getExpr());
			}
		} else if (x.length() > 2 && x.substring(0,2).equals("mt")) { // mt
			if (x.length() < 8) {
				throw new IllegalLineException("Input is too short: mt is followed by blank space, a LineNumber, then blank space, another LineNumber, blank space, then an expression");
			} else if (x.charAt(2) != ' ') {
				throw new IllegalLineException("mt has to be followed by blank space");
			} else {
				int spacePos2 = checkTwoLineSpace(x);
				Expression expr = mtPattern(subX2, spacePos2);
				afterShow = false;
				myAssumptions.add(expr.getExpr());
			}
		} else if (theoremSet.getNames().contains(x.substring(0, firstSpacePos(x)))) { // theorem use
			if (firstSpacePos(x) == x.length()) {
				throw new IllegalLineException("TheoremName has to be followed by blank space, then an expression");
			}
			Expression expr = new Expression(x.substring(firstSpacePos(x)).trim());
			Expression thmExpr = theoremSet.getTheorems().get(theoremSet.getNames().indexOf(x.substring(0, firstSpacePos(x))));
			if (!expr.matchPattern(thmExpr.getExprPattern())) {
				throw new IllegalInferenceException("Expression doesn't match the pattern of the theorem");
			} else {
				if (expr.findMainImply() == 0 || !myAssumptions.contains(expr.getExpr().substring(1, expr.findMainImply()))) {
					for (int i = 0; i < expr.getToCheck().size(); i++) {
						if (!myAssumptions.contains(expr.getToCheck().get(i))) {
							throw new IllegalInferenceException("Illegal call of theorem: one of the sub-parts not valid yet");
						}
					}
				}
			}
			afterShow = false;
			myAssumptions.add(expr.getExpr());
		} else {
			throw new IllegalInferenceException("Reason not valid: has to be one of 'print','show','assume','repeat','ic','co','mp','mt' or a theorem name supplied, followed by blank space");
		}
		myLatestLine = x;
		this.storeExpr();
		this.addLineNum();
		this.changeLineNum();
		myProofSoFar = myProofSoFar + " " + myLatestLine;
	}

	@Override
	public String toString() {
		return myProofSoFar; 

	}
	
	public boolean isComplete() {
		if (myLatestLine.contains("show") || myLatestLine.contains("assume")) {
			return false;
		} else {
			if (myLatestLine.contains("repeat") && myAssumptions.contains(myLatestLine.substring(myLatestLine.indexOf("("), myLatestLine.length() - 1).trim())) {
				return true;
			}
			if (myLatestLine.trim().endsWith(" " + myOutermostExp) && prevlinenos.get(prevlinenos.size() - 1).length() == 1

					&& nextNum > 2 && !inSubproof) {
				return true;
			} else {
				return false;
			}
		}
	}

	private void print() { 
		String myProofOutput = this.toString();
		if (!myProofOutput.contains("*"))
		{
			System.out.println(myProofOutput);
			return;
		}
		while (myProofOutput.contains("*")) {
			System.out.println(myProofOutput.substring(0,
					myProofOutput.indexOf("*") - 1).trim());
			myProofOutput = myProofOutput
					.substring(myProofOutput.indexOf("*") + 1);
		}
		System.out.println(myProofOutput);
	}
	
	private int checkOneLineSpace (String s) throws IllegalLineException {
		int spacePos = 0;
		boolean spaceFound = false;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ' ') {
				spaceFound = true;
				spacePos = i;
				break;
			}
		}
		if (!spaceFound) {
			throw new IllegalLineException("LineNumber and expression have to be separated by blank space");
		} else {
			lineNum = s.substring(0, spacePos);
			if (!prevlinenos.contains(lineNum) || lineNum.length() > nextLineNumber().toString().length()) {
				throw new IllegalLineException("LineNumber is not valid");
			}
		}
		return spacePos;
	}
	
	private int checkTwoLineSpace (String x) throws IllegalLineException {
		int spacePos2 = 0;
		String subX = x.substring(3).trim();
		boolean spaceFound1 = false;
		int spacePos1 = 0;
		for (int i = 0; i < subX.length(); i++) {
			if (subX.charAt(i) == ' ') {
				spaceFound1 = true;
				spacePos1 = i;
				break;
			}
		}
		if (!spaceFound1) {
			throw new IllegalLineException("LineNumber1 and LineNumber2 have to be separated by blank space");
		} else {
			lineNum1 = subX.substring(0, spacePos1);
			if (!prevlinenos.contains(lineNum1) || lineNum1.length() > nextLineNumber().toString().length()) {
				throw new IllegalLineException("First LineNumber is not valid");
			}
			subX2 = subX.substring(spacePos1).trim();
			boolean spaceFound2 = false;				
			for (int i = 0; i < subX2.length(); i++) {
				if (subX2.charAt(i) == ' ') {
					spaceFound2 = true;
					spacePos2 = i;
					break;
				}
			}
			if (!spaceFound2) {
				throw new IllegalLineException("LineNumber2 and expression have to be separated by blank space");
			} else {
				lineNum2 = subX2.substring(0, spacePos2);
				if (!prevlinenos.contains(lineNum2) || lineNum2.length() > nextLineNumber().toString().length()) {

					throw new IllegalLineException("Second LineNumber is not valid");
				}
			}
		}
		return spacePos2;
	}
	
	private static int firstSpacePos (String s) {
		int spacePos = s.length();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ' ') {
				spacePos = i;
				break;
			}
		}
		return spacePos;
	}

	private void changeLineNum() {
		if (nextNum == 1) {
			myOutermostExp = myLatestLine.trim().substring(4).trim();
		}
		if (nextNum != 1 && myLatestLine.contains("show")) {
			afterShow = true;
			subproofs.add(" " + myLatestLine.trim().substring(4).trim());
			index = 1;
			if (nextSubNum.length() == 1) {
				nextSubNum = nextNum + "." + "1";
			} else {
				nextSubNum = nextSubNum + "." + "1";
			}
			inSubproof = true;
			matchproof++;
		}

		if (inSubproof && !myLatestLine.contains("show")) {
			if (matchproof != -1
					&& myLatestLine.endsWith(subproofs.get(matchproof))) {
				myAssumptions.add(subproofs.get(matchproof).trim());
				if (index < 10) {
					nextSubNum = nextSubNum.substring(0,
							nextSubNum.length() - 2);
					if (nextSubNum.length() > 2) {
						index = Character.getNumericValue(nextSubNum
								.charAt(nextSubNum.length() - 1));
					}
				} else {
					nextSubNum = nextSubNum.substring(0,
							nextSubNum.length() - 3);
					index = Character.getNumericValue(nextSubNum
							.charAt(nextSubNum.length() - 1));
				}
				if (nextSubNum.length() <= 2) {
					inSubproof = false;
					nextSubNum = nextNum + "";
				} else {
					index++;
					nextSubNum = nextSubNum.substring(0,
							nextSubNum.length() - 1);
					nextSubNum = nextSubNum + index;
				}
				subproofs.remove(subproofs.size() - 1);
				matchproof--;
			} else {
				index++;
				if (index > 10) {
					nextSubNum = nextSubNum.substring(0,
							nextSubNum.length() - 2);
					nextSubNum = nextSubNum + index;
				} else {
					nextSubNum = nextSubNum.substring(0,
							nextSubNum.length() - 1);
					nextSubNum = nextSubNum + index;
				}
			}
		}

		if (!inSubproof) {
			nextNum++;
		}
	}
	
	private void storeExpr() {
		String myExpr = "";
		boolean foundparen = false;
		if (!myLatestLine.contains("(") && !myLatestLine.contains("~")) {
			myExpr = String
					.valueOf(myLatestLine.charAt(myLatestLine.length() - 1));
		} else {
			for (int j = 0; !foundparen; j++) {
				if (String.valueOf(myLatestLine.charAt(j)).equals("(")
						|| String.valueOf(myLatestLine.charAt(j)).equals("~")) {
					foundparen = true;
					for (int k = j; k < myLatestLine.length(); k++) {
						myExpr = myExpr + myLatestLine.charAt(k);
					}
				}
			}
		}
		prevexpr.add(myExpr.trim());
	}
	
	private void addLineNum() {
		if (onNum) {
			prevlinenos.add(String.valueOf(nextNum));
			myProofSoFar = myProofSoFar + " " + "*" + String.valueOf(nextNum);
		} else {
			prevlinenos.add(nextSubNum);
			myProofSoFar = myProofSoFar + " " + "*" + nextSubNum;
		}
	}
	
	private Expression icPattern(String subX, int spacePos)
			throws IllegalInferenceException, IllegalLineException {
		Expression expr = new Expression(subX.substring(spacePos).trim());
		int opPos = expr.findMainImply();
		if (opPos == 0) {
			throw new IllegalInferenceException(
					"Illegal inference: ic means if E2, can derive any (E1=>E2)");
		}
		new Expression(expr.getExpr().substring(1, opPos));
		Expression expr1 = new Expression(expr.getExpr().substring(opPos + 2,
				expr.getExpr().length() - 1));
		if (!expr1.equals(prevexpr.get(prevlinenos.indexOf(lineNum)))) {
			throw new IllegalLineException("Expressions doesn't match");
		}
		if (!myAssumptions.contains(expr1.getExpr())) {
			throw new IllegalInferenceException(
					"Illegal call of 'ic', latter part not valid");
		}
		return expr;
	}

	private Expression coPattern(String subX2, int spacePos2)
			throws IllegalInferenceException, IllegalLineException {
		Expression expr1 = new Expression(prevexpr.get(prevlinenos
				.indexOf(lineNum1)));
		if (!myAssumptions.contains(expr1.getExpr())) {
			throw new IllegalInferenceException(
					"Illegal call of 'co', expression referred by first LineNumber not valid");
		}
		Expression expr2 = new Expression(prevexpr.get(prevlinenos
				.indexOf(lineNum2)));
		if (!myAssumptions.contains(expr2.getExpr())) {
			throw new IllegalInferenceException(
					"Illegal call of 'co', expression referred by second LineNumber not valid");
		}
		if (!expr1.isNegation(expr2.getExpr())) {
			throw new IllegalInferenceException(
					"Illegal call of 'co', two expressions are not negations");
		}
		Expression expr = new Expression(subX2.substring(spacePos2).trim());
		return expr;
	}

	private Expression mpPattern(String subX2, int spacePos2)
			throws IllegalInferenceException, IllegalLineException {
		Expression expr1 = new Expression(prevexpr.get(prevlinenos
				.indexOf(lineNum1)));
		if (!myAssumptions.contains(expr1.getExpr())) {
			throw new IllegalInferenceException(
					"Illegal call of 'mp', expression referred by first LineNumber not valid");
		}
		Expression expr2 = new Expression(prevexpr.get(prevlinenos
				.indexOf(lineNum2)));
		if (!myAssumptions.contains(expr2.getExpr())) {
			throw new IllegalInferenceException(
					"Illegal call of 'mp', expression referred by second LineNumber not valid");
		}
		Expression expr = new Expression(subX2.substring(spacePos2).trim());
		int opPos1 = expr1.findMainImply();
		int opPos2 = expr2.findMainImply();
		if (opPos1 == 0 && opPos2 == 0) {
			throw new IllegalInferenceException(
					"Illegal inference: mp means if E1 and E1=>E2, can infer E2");
		}
		if (!(opPos1 != 0 && expr2.equals(expr1.getExpr().substring(1, opPos1)) && expr
				.equals(expr1.getExpr().substring(opPos1 + 2,
						expr1.getExpr().length() - 1)))
				&& !(opPos2 != 0
						&& expr1.equals(expr2.getExpr().substring(1, opPos2)) && expr
							.equals(expr2.getExpr().substring(opPos2 + 2,
									expr2.getExpr().length() - 1)))) {
			throw new IllegalInferenceException(
					"Illegal inference: mp means if E1 and E1=>E2, can infer E2");
		}
		return expr;
	}

	private Expression mtPattern(String subX2, int spacePos2)
			throws IllegalInferenceException, IllegalLineException {
		Expression expr1 = new Expression(prevexpr.get(prevlinenos
				.indexOf(lineNum1)));
		if (!myAssumptions.contains(expr1.getExpr())) {
			throw new IllegalInferenceException(
					"Illegal call of 'mt', expression referred by first LineNumber not valid");
		}
		Expression expr2 = new Expression(prevexpr.get(prevlinenos
				.indexOf(lineNum2)));
		if (!myAssumptions.contains(expr2.getExpr())) {
			throw new IllegalInferenceException(
					"Illegal call of 'mt', expression referred by second LineNumber not valid");
		}
		Expression expr = new Expression(subX2.substring(spacePos2).trim());
		int opPos1 = expr1.findMainImply();
		int opPos2 = expr2.findMainImply();
		if (opPos1 == 0 && opPos2 == 0) {
			throw new IllegalInferenceException(
					"Illegal inference: mt means if ~E2 and E1=>E2, can infer ~E1");
		}
		if (!(opPos1 != 0
				&& expr2.isNegation(expr1.getExpr().substring(opPos1 + 2,
						expr1.getExpr().length() - 1)) && expr.isNegation(expr1
				.getExpr().substring(1, opPos1)))
				&& !(opPos2 != 0
						&& expr1.isNegation(expr2.getExpr().substring(
								opPos2 + 2, expr2.getExpr().length() - 1)) && expr
							.isNegation(expr2.getExpr().substring(1, opPos2)))) {
			throw new IllegalInferenceException(
					"Illegal inference: mt means if ~E2 and E1=>E2, can infer ~E1");
		}
		return expr;
	}
	
	private void assumePattern(Expression expr) throws IllegalInferenceException{
		String showExpr = prevexpr.get(prevexpr.size()-1); // previous 'show' expression
		String exprStr = expr.getExpr(); // expression to be assumed
		if (expr.isNegation(showExpr)) {
		} else if (showExpr.contains(exprStr)) {
			String str = new String(showExpr);
			int nextInd = 0;
			boolean foundImply = false;
			while (nextInd < str.length()) {					
				str = str.substring(nextInd);
				int occur = str.indexOf(exprStr);
				if (occur == -1 || occur+exprStr.length()+2>str.length()) {
					break;
				} else if (str.charAt(occur-1) == '(' && str.substring(occur+exprStr.length(), occur+exprStr.length()+2).equals("=>")) {
					foundImply = true;
					break;
				} else {
					nextInd = occur+exprStr.length();
				}
			} if (!foundImply) {
				throw new IllegalInferenceException("Can only assume part in 'show' that's before '=>'");
			}
		} else {
			throw new IllegalInferenceException("Invalid assumption");
		}
	}
}