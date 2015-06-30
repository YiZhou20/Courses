package edu.berkeley.cs.proj2;

import java.util.*;

public class Expression {

	// List of valid variables (lower-case letters)

	private static List Letters = new ArrayList(Arrays.asList('a', 'b', 'c',
			'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));

	private static List OtherValidChars = new ArrayList(Arrays.asList('(', ')',
			'~', '&', '|', '=', '>'));

	// String representing an instance of an Expression

	private String expr;
	private ArrayList<String> exprToCheck = new ArrayList<String> ();
	private int index = -1;

	public String getExpr() {

		return this.expr;

	}

	// Initializes Expression and uses various static methods to check validity

	public Expression(String s) throws IllegalLineException {

		List ListExpression = StringToList(s);

		CheckSpaces(ListExpression);

		CheckChars(ListExpression);

		CheckParenNumber(ListExpression);

		CheckExpression(ListExpression);

		expr = new String(s);

	}

	// this converts he string into an array of chars, and then into an
	// ArrayList

	// this also checks if there is leading or trailing whitespace

	public static ArrayList StringToList(String exp)
			throws IllegalLineException {

		int initialLength = exp.length();

		exp = exp.trim();

		if (initialLength != exp.length()) {

			throw new IllegalLineException("Expression may not contain spaces");

		}

		char[] tempCharArray = exp.toCharArray();

		List thisExpression = new ArrayList();

		for (int i = 0; i < tempCharArray.length; i++) {

			thisExpression.add(tempCharArray[i]);

		}

		return (ArrayList) thisExpression;

	}

	// Checks for null and for Expressions with spaces

	public static void CheckSpaces(List exp) throws IllegalLineException {

		if (exp.size() == 0) {

			throw new IllegalLineException("Expression cannot be null");

		}

		if (exp.contains(' ')) {

			throw new IllegalLineException("Expression may not contain spaces");

		}

	}

	// Checks that the open and closed parenthesis of the Expression are
	// balanced

	// Also checks if characters are outside parenthesis on both sides

	public static void CheckParenNumber(List exp) throws IllegalLineException {

		if (exp.contains('(')
				&& (!exp.get(0).equals('(') && !exp.get(0).equals('~'))) {

			throw new IllegalLineException(
					"Expression must be legally formed with chracters inside parenthesis");

		}

		int leftBracketCount = 0;

		int rightBracketCount = 0;

		for (int i = 0; i < exp.size(); i++) {

			if (exp.get(i).equals('(')) {

				leftBracketCount++;

			}

			if (exp.get(i).equals(')')) {

				rightBracketCount++;

				if ((rightBracketCount > leftBracketCount)
						|| (rightBracketCount >= leftBracketCount && i != exp
								.size() - 1)) {

					throw new IllegalLineException(
							"Incomplete use of parenthesis or use of characters outside parenthesis");

				}

			}

		}

		if (leftBracketCount != rightBracketCount) {

			throw new IllegalLineException("Parenthesis are unbalanced");

		}

	}

	// Checks that only valid character are being used

	public static void CheckChars(List exp) throws IllegalLineException {

		for (int i = 0; i < exp.size(); i++) {

			if (!Letters.contains(exp.get(i))
					&& !OtherValidChars.contains(exp.get(i))) {

				throw new IllegalLineException(
						"Invalid character/s. Expression may only contain lower-case letters, parenthesis, and operators (~, &, |, =>)");

			}

		}

	}

	// Checks that this Expression is valid and follows the formatting rules

	public static void CheckExpression(List exp) throws IllegalLineException {

		if (exp.size() == 0) {

			throw new IllegalLineException(
					"You have attempted enter an input with a null expression inside");

		}

		// if the expression begins with (~) then the following must be a valid
		// expression

		if (exp.get(0).equals('~')) {

			CheckExpression(exp.subList(1, exp.size()));

		}

		// if the expression begins and ends with parenthesis, it must have an
		// operator

		// the expressions on each side of the operator must be valid

		else if (exp.get(0).equals('(') && exp.get(exp.size() - 1).equals(')')) {

			if (HasOperatorAndOr(exp)) {

				int halfway = OperatorPosition(exp);

				int end = exp.size() - 1;

				CheckExpression(exp.subList(1, halfway));

				CheckExpression(exp.subList(halfway + 1, end));

			}

			else if (HasOperatorImply(exp)) {

				int halfway = OperatorPosition(exp);

				int end = exp.size() - 1;

				CheckExpression(exp.subList(1, halfway));

				CheckExpression(exp.subList(halfway + 2, end));

			}

			else {

				throw new IllegalLineException(
						"An Expression is using parenthesis with no operator");

			}

		}

		// if the expression is one character, it must be a lower-case letter

		else if (exp.size() == 1) {

			if (!Letters.contains(exp.get(0))) {

				throw new IllegalLineException(
						"a single variable expression is not using a lower-case letter");

			}

			return;

		}

		else {

			throw new IllegalLineException(
					"Expressions is bracketed incorrectly or has consecutive variables");

		}

	}

	// Determines whether Expression has an operator (& or |)

	public static boolean HasOperatorAndOr(List exp)
			throws IllegalLineException {

		try {

			if (exp.get(0).equals('(') && exp.get(exp.size() - 1).equals(')')) {

				int tracker = 0;

				for (int i = 0; i < exp.size(); i++) {

					if (exp.get(i).equals('('))
						tracker++;

					if (exp.get(i).equals(')'))
						tracker--;

					if ((exp.get(i).equals('&') || exp.get(i).equals('|'))
							&& tracker == 1) {

						return true;

					}

				}

			}

			return false;

		} catch (Exception e) {

			return false;

		}

	}

	// Determines whether Expression has an operator (=>)

	public static boolean HasOperatorImply(List exp)
			throws IllegalLineException {

		try {

			if (exp.get(0).equals('(') && exp.get(exp.size() - 1).equals(')')) {

				int tracker = 0;

				for (int i = 0; i < exp.size(); i++) {

					if (exp.get(i).equals('('))
						tracker++;

					if (exp.get(i).equals(')'))
						tracker--;

					if ((exp.get(i).equals('=') && exp.get(i + 1).equals('>'))
							&& tracker == 1) {

						return true;

					}

				}

			}

			return false;

		} catch (Exception e) {

			return false;

		}

	}

	// Returns an integer that gives the index of the first or only character of
	// the operator in the Expression, if it has one

	public static int OperatorPosition(List exp) throws IllegalLineException {

		if (HasOperatorAndOr(exp) || HasOperatorImply(exp)) {

			int tracker = 0;

			for (int i = 0; i < exp.size(); i++) {

				if (exp.get(i).equals('('))
					tracker++;

				if (exp.get(i).equals(')'))
					tracker--;

				if ((exp.get(i).equals('&') || exp.get(i).equals('|') || exp
						.get(i).equals('=')) && tracker == 1) {

					return i;

				}

			}

		}

		return 0;

	}

	public ArrayList<String> getToCheck () {
		return exprToCheck;
	}
	
	public boolean equals(Expression exprToCompare) {

		return expr.equals(exprToCompare.expr);

	}

	public boolean equals(String exprToCompare) {

		return expr.equals(exprToCompare);

	}

	public boolean isNegation(String exprToCompare) {

		String neg = new String("~");

		return exprToCompare.equals(neg.concat(expr)) ||

		expr.equals(neg.concat(exprToCompare));

	}

	public int findMainImply() {

		int nesting = 0;

		int opPos = 0;

		for (int k = 1; k < expr.length() - 3; k++) {

			if (expr.charAt(k) == '(') {

				nesting++;

			}

			if (expr.charAt(k) == ')') {

				nesting--;

			}

			if (nesting == 0 && expr.substring(k + 1, k + 3).equals("=>")) {

				opPos = k + 1;

				break;

			}

		}

		return opPos;

	}
	
	public BinaryTree getExprPattern () {
		return BinaryTree.exprTree(expr);
	}
	
	public boolean matchPattern (BinaryTree t) throws IllegalLineException {
		boolean matches = matchPatternHelper (expr, t);
		return matches;
	}
	
	private boolean matchPatternHelper (String s, BinaryTree t) throws IllegalLineException {
		if (!s.contains("(") && !s.contains("~")) {
			return false;
		} else if (s.charAt(0) == '~') {
			int subPos = 1;
			for (int i = 1; i < s.length(); i++) {
				if (s.charAt(i) != '~') {
					subPos = i;
					break;
				}
			}
			if (!s.substring(0, subPos).contains(t.myRoot.myItem.toString())) {
				return false;
			} else if (t.myRoot.myLeft == null) {
				subPos = t.myRoot.myItem.toString().length();
				Expression expr = new Expression (s.substring(subPos));
				index = index+1;
				exprToCheck.add(index, expr.getExpr());
				return true;
			} else if (s.charAt(subPos) != '(') {
				if (t.myRoot.myLeft != null) {
					return false;
				} else {
					Expression expr = new Expression (s.substring(subPos));
					index = index+1;
					exprToCheck.add(index, expr.getExpr());
					return true;
				}
			} else {
				return matchPatternHelper (s.substring(subPos), new BinaryTree(t.myRoot.myLeft));
			}
		} else {
	        int nesting = 0;
	        int opPos = 0;
	        for (int k=1; k<s.length()-1; k++) {        	
	            if (s.charAt(k) == '(') {
					nesting++;
				}
	            if (s.charAt(k) == ')') {
					nesting--;
				}
	            if (nesting == 0) {
	            	if (s.charAt(k) == '~') {
	            		for (int i = k; i < s.length(); i++) {
	    					if (s.charAt(i) != '~') {
	    						k = i;
	    						break;
	    					}
	    				}
	            	}
					opPos = k + 1;
					break;
				} 
	        }	        
	        String opnd1 = s.substring (1, opPos);
	        String opnd2;
	        String op;
	        if (s.charAt(opPos) == '=') {
	        	opnd2 = s.substring (opPos+2, s.length()-1);
		        op = s.substring (opPos, opPos+2);
			} else {
				opnd2 = s.substring (opPos+1, s.length()-1);
				op = s.substring (opPos, opPos+1);
			}
	        if (!op.equals(t.myRoot.myItem.toString())) {
				return false;
			} else if (t.myRoot.myLeft == null) {
				Expression expr = new Expression (opnd1);
				index = index+1;
				exprToCheck.add(index, expr.getExpr());
				if (t.myRoot.myRight == null) {
					expr = new Expression (opnd2);
					index = index+1;
					exprToCheck.add(index, expr.getExpr());
					return true;
				} else if (opnd2.charAt(0) != '('  && opnd2.charAt(0) != '~') {
					return false;
				} else {
					return matchPatternHelper (opnd2, new BinaryTree(t.myRoot.myRight));
				}
			} else if (opnd1.charAt(0) != '('  && opnd1.charAt(0) != '~') {
				return false;
			} else if (t.myRoot.myRight == null) {
				Expression expr = new Expression (opnd2);
				index = index+1;
				exprToCheck.add(index, expr.getExpr());
				return matchPatternHelper (opnd1, new BinaryTree(t.myRoot.myLeft));
			} else if (opnd2.charAt(0) != '('  && opnd2.charAt(0) != '~') {
				return false;
			} else {
				return matchPatternHelper (opnd1, new BinaryTree(t.myRoot.myLeft)) && matchPatternHelper (opnd2, new BinaryTree(t.myRoot.myRight));
			}
		}
	}
	
	public static class BinaryTree {

		private TreeNode myRoot;
		
		public BinaryTree ( ) {
			myRoot = null;
		}
		
		public BinaryTree (TreeNode t) {
			myRoot = t;
		}
		
		private static class TreeNode {
			
			private Object myItem;
			private TreeNode myLeft;
			private TreeNode myRight;
			
			public TreeNode (Object obj) {
				myItem = obj;
				myLeft = myRight = null;
			}

		}
		
		public static BinaryTree exprTree (String s) {
		    BinaryTree result = new BinaryTree ( );
		    result.myRoot = result.exprTreeHelper (s);
		    return result;
		}
		
		private TreeNode exprTreeHelper (String expr) {
			if (expr.charAt(0) == '~') {
				int subPos = 1;
				for (int i = 1; i < expr.length(); i++) {
					if (expr.charAt(i) != '~') {
						subPos = i;
						break;
					}
				}
				TreeNode exprTreeNode = new TreeNode(expr.substring(0, subPos));
				if (expr.charAt(subPos) != '(') {
					exprTreeNode.myLeft = null;
				} else {
					exprTreeNode.myLeft = exprTreeHelper(expr.substring(subPos));
				}
				return exprTreeNode;
			} else {
		        int nesting = 0;
		        int opPos = 0;
		        for (int k=1; k<expr.length()-1; k++) {
		        	
		            if (expr.charAt(k) == '(') {
						nesting++;
					}
		            if (expr.charAt(k) == ')') {
						nesting--;
					}
		            if (nesting == 0) {
		            	if (expr.charAt(k) == '~') {
		            		for (int i = k; i < expr.length(); i++) {
		    					if (expr.charAt(i) != '~') {
		    						k = i;
		    						break;
		    					}
		    				}
		            	}
						opPos = k + 1;
						break;
					}
		            
		        }
		        
		        String opnd1 = expr.substring (1, opPos);
		        String opnd2;
		        String op;
		        if (expr.charAt(opPos) == '=') {
		        	opnd2 = expr.substring (opPos+2, expr.length()-1);
			        op = expr.substring (opPos, opPos+2);
				} else {
					opnd2 = expr.substring (opPos+1, expr.length()-1);
					op = expr.substring (opPos, opPos+1);
				}
		        TreeNode exprTreeNode = new TreeNode(op);
		        if(opnd1.charAt(0) != '(' && opnd1.charAt(0) != '~') {
		        	exprTreeNode.myLeft = null;
		        } else {
		        	exprTreeNode.myLeft = exprTreeHelper(opnd1);
		        }
		        if(opnd2.charAt(0) != '(' && opnd2.charAt(0) != '~') {
		        	exprTreeNode.myRight = null;
		        } else {
		        	exprTreeNode.myRight = exprTreeHelper(opnd2);
		        }
		        return exprTreeNode;
		    }
		}
	}
}