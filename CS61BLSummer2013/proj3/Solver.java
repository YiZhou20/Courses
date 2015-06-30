
/**
 * CS61BL Project3 Sliding-Block Puzzle Solver
 * 
 * @author Anuli Kshetramade (og)
 * @author Rohan Sachdev (dy)
 * @author Spencer Axelrod (do)
 * @author Yi Zhou (ds)
 */

/**
 * This class contains the main method, takes in command-line inputs, finds a solution if the provided puzzle is solvable
 * @param args [optional debug info request], initial puzzle tray, puzzle goal configuration
 * moving directions: 0/up; 1/down; 2/left; 3/right
 * isOK() method called if any of the debug options is on
 * block numberings are the order counts of their input, i.e. block 1 is the first line of 4 in initial config input
 */

import java.util.*;

public class Solver {

	public static void main(String[] args) {
		String debugOp;
		String initConfig;
		String goalConfig;
		// debugging flags
		boolean solvable = false;
		boolean process = false;
		boolean dprocess = false;
		boolean step = false;
		long elapse = 0;
		Timer t = new Timer(); // Timer for debugging output
		t.start();
		// check for illegal inputs
		if (args.length != 3 && args.length != 2) {
			System.out.println ("Wrong number of arguments");
			System.exit(1);
		}
		if (args.length == 3) {
            debugOp = args[0];
            if (!debugOp.equals("-ooptions") && !debugOp.equals("-osolvable") && !debugOp.equals("-oprocess") && !debugOp.equals("-odprocess") && !debugOp.equals("-osteps")) {
				System.out.println("Invalid debug command, type 'options' to see description of all possible choices");
				System.exit(1);
			}
            if (debugOp.equals("-ooptions")) {
				System.out.println("-osolvable: prints whether a puzzle is solvable");
				System.out.println("-oprocess: prints solving process -- initializing, breadth search, depth search, printing result etc");
				System.out.println("-odprocess: detailed process -- get possible moves, add tray to collection, compare to goal etc");
				System.out.println("-osteps: prints moving steps in terms of directions");
				System.exit(0);
			}
            if (debugOp.equals("-osolvable")) {
				solvable = true;
			}
            if (debugOp.equals("-oprocess")) {
				process = true;
			}
            if (debugOp.equals("-odprocess")) {
				dprocess = true;
			}
            if (debugOp.equals("-osteps")) {
				step = true;
			}
            args[0] = args[1];
            args[1] = args[2];
            args[2] = null;
		}
		initConfig = args[0];
		goalConfig = args[1];
		// initialize puzzle tray
		if (process || dprocess) {
			System.out.println("Initializing tray ... ");
		}
		InputSource initIn = new InputSource(initConfig);
		String size = initIn.readLine();
		Tray init = new Tray(size);
		for (String s = initIn.readLine(); s != null; s = initIn.readLine()) {
			init.addBlock(s);
			init.markOccupied(s);
		}
		// set up goal config
		if (process || dprocess) {
			System.out.println("Setting up goal configurations ... ");
		}
		InputSource goalIn = new InputSource(goalConfig);
		Tray goal = new Tray(size);
		for (String s = goalIn.readLine(); s != null; s = goalIn.readLine()) {
			goal.addBlock(s);
			goal.markOccupied(s);
		}
		if (process || dprocess) {
			System.out.println("Checking if puzzle is initially solved ... ");
		}
		if (init.isComplete(goal)) {
			if (solvable) {
				System.out.println("Puzzle is solvable");
			}
			if (process || dprocess) {
				System.out.println("Initially solved!");
			}
			if (step) {
				System.out.println("Puzzle is initially solved, no steps necessary");
			}
			System.exit(0);
		}
		// find solution
		if (process || dprocess) {
			System.out.println("Starting searching process ... ");
		}
		Stack<Tray> myTrays = new Stack<Tray> ();
		HashSet<Tray> seen = new HashSet <Tray> ( );
		Tray result = new Tray("0 0");
		seen.add(init);
		// breadth first track down 10 levels
		if (process || dprocess || step) {
			System.out.println("Process breadth first search for 10 levels ... ");
		}
		ArrayList<Tray> parTrays = new ArrayList<Tray> ();
		parTrays.add(init);
		ArrayList<Tray> subTrays = new ArrayList<Tray> ();
		subTrays.add(init);
		for (int k = 0; k < 5; k++) {
			if (dprocess) {
				System.out.println("Breadth first search at level " + k);
			}
			parTrays = subTrays;
			subTrays = new ArrayList<Tray>();
			for (int i = 0; i < parTrays.size(); i++) {
				if (dprocess) {
					System.out.println("Getting unseen trays by a single move form tray " + i);
				}
				boolean[][] subMoves = parTrays.get(i).possibleMoves();
				for (int j = 0; j < subMoves.length; j++) {
					for (int j2 = 0; j2 < 4; j2++) {
						if (subMoves[j][j2]) {
							Tray temp = parTrays.get(i).moveBlock(j, j2);
							if (solvable || process || dprocess || step) {
								try {
									temp.isOK();
								} catch (IllegalStateException e) {
									System.out.println(e.getMessage());
									System.exit(1);
								}
							}
							if (temp.isComplete(goal)) {
								elapse = t.stop();
								if (solvable) {
									System.out.println("Puzzle is solvable");
								}
								if (process || dprocess || step) {
									System.out.println("Solved!");
									System.out.println("Printing solution moves ... ");
									System.out.println("Runtime: " + elapse + " milliseconds");
								}
								result = temp;
								printMoves(result.getTrack());
								System.exit(0);
							} else if (!seen.contains(temp)) {
								seen.add(temp);
								subTrays.add(temp);
							}
						}
					}
				}
			}
		}
		if (process || dprocess) {
			System.out.println("Sorting breadth first search resulting trays by distance to goal ... ");
		}
		ArrayList<Tray> breadthTrays = new ArrayList<Tray>();
		ArrayList<Integer> breadthDists = new ArrayList<Integer> ();
		for (int i = 0; i < subTrays.size(); i++) {
			boolean[][] breadthMoves = subTrays.get(i).possibleMoves();
			for (int j = 0; j < breadthMoves.length; j++) {
				for (int j2 = 0; j2 < 4; j2++) {
					if (breadthMoves[j][j2]) {
						Tray temp = subTrays.get(i).moveBlock(j, j2);
						if (solvable || process || dprocess || step) {
							try {
								temp.isOK();
							} catch (IllegalStateException e) {
								System.out.println(e.getMessage());
								System.exit(1);
							}
						}
						if (temp.isComplete(goal)) {
							elapse = t.stop();
							if (solvable) {
								System.out.println("Puzzle is solvable");
							}
							if (process || dprocess || step) {
								System.out.println("Solved!");
								System.out.println("Printing solution moves ... ");
								System.out.println("Runtime: " + elapse + " milliseconds");
							}
							result = temp;
							printMoves(result.getTrack());
							System.exit(0);
						} else if (!seen.contains(temp)) {
							seen.add(temp);
							int trayDist = temp.getDist(goal);
							if (breadthDists.isEmpty()) {
								breadthDists.add(trayDist);
								breadthTrays.add(temp);
							} else {
								int trayPos = 0;
								while (trayPos < breadthDists.size() && trayDist < breadthDists.get(trayPos)) {
									trayPos++;
								}
								breadthDists.add(trayPos, trayDist);
								breadthTrays.add(trayPos,temp);
							}
						}
					}
				}
			}
		}
		if (process || dprocess || step) {
			System.out.println("Entering depth first traversal starting from tray closest to goal ... ");
		}
		if (dprocess) {
			System.out.println("Adding breadth first search results to stack ... ");
		}
		for (int i = 0; i < breadthTrays.size(); i++) {
			myTrays.push(breadthTrays.get(i));
		}
		// depth first track
		boolean solutionFound = false;
		boolean reEnter = false;
		if (step) {
			Tray top = myTrays.peek();
			System.out.println("Move block " + top.getBlockPos() + " " + top.getBlockDir());
			System.out.println("Current closest tray to goal:");
			for (int i = 0; i < top.getBlocks().size(); i++) {
				int[] closest = top.getBlocks().get(i);
				System.out.println(closest[0] + " " + closest[1] + " " + closest[2] + " " + closest[3]);
			}
		}
		while (!myTrays.empty()) {
			if (dprocess) {
				System.out.println("Getting the top tray ... ");
			}
			Tray cur = myTrays.pop();
			if (step) {
				if (reEnter) {
					System.out.println("Move block " + cur.getBlockPos() + " " + cur.getBlockDir());
				}
				System.out.println("Distance to goal " + cur.getDistance());
				reEnter = true;
			}
			if (dprocess) {
				System.out.println("Iterating through possible moves ... ");
			}
			boolean[][] moves = cur.possibleMoves();
			ArrayList<Tray> trays = new ArrayList<Tray>();
			ArrayList<Integer> dists = new ArrayList<Integer> ();
			for (int i = 0; i < moves.length; i++) {
				for (int j = 0; j < 4; j++) {
					if (moves[i][j]) {
						if (dprocess) {
							System.out.println("Try moving block " + (i+1) + " in direction " + j);
						}
						Tray temp = cur.moveBlock(i, j);
						if (dprocess) {
							System.out.println("Verifying generated tray ... ");
						}
						if (solvable || process || dprocess || step) {
							try {
								temp.isOK();
							} catch (IllegalStateException e) {
								System.out.println(e.getMessage());
								System.exit(1);
							}
						}
						if (dprocess) {
							System.out.println("Checking if goal is reached ... ");
							System.out.println("or checking if tray is seen before ... ");
						}
						if (temp.isComplete(goal)) {
							elapse = t.stop();
							result = temp;
							solutionFound = true;
							if (solvable) {
								System.out.println("Puzzle is solvable");
							}
							if (process || dprocess || step) {
								System.out.println("Solved!");
								System.out.println("Printing solution moves ... ");
							}
							break;
						} else if (!seen.contains(temp)) {
							if (dprocess) {
								System.out.println("Calculating distance of unseen tray to goal ... storing ... ");
							}
							int trayDist = temp.getDist(goal);
							if (dists.isEmpty()) {
								dists.add(trayDist);
								trays.add(temp);
							} else {
								int trayPos = 0;
								while (trayPos < dists.size() && trayDist < dists.get(trayPos)) {
									trayPos++;
								}
								dists.add(trayPos, trayDist);
								trays.add(trayPos,temp);
							}
						}
					}
				}
				if (solutionFound) {
					break;
				}
			}
			if (solutionFound) {
				break;
			}
			if (dprocess) {
				System.out.println("Adding sorted sub-trays to stack collection ... ");
			}
			if (step && trays.isEmpty()) {
				System.out.println("Dead-end! Unmaking most recent move ... ");
			}
			for (int i = 0; i < trays.size(); i++) {
				myTrays.push(trays.get(i));
				seen.add(trays.get(i));
			}
		}
		if (!solutionFound) {
			if (solvable) {
				System.out.println("Puzzle is not solvable");
			}
			if (process || dprocess) {
				System.out.println("Search complete. Puzzle is not solvable. Exiting ... ");
			}
			if (step) {
				System.out.println("Search complete. No solution found");
			}
			System.exit(1);
		} else {
			printMoves(result.getTrack());
			if (process || dprocess || step) {
				System.out.println("Runtime: " + elapse + " milliseconds");
			}
		}
	}
	
	private static void printMoves(LinkedList <int [ ]> moves) {
		while (!moves.isEmpty()) {
			int[] toPrint = moves.getLast();
			System.out.println(toPrint[0] + " " + toPrint[1] + " " + toPrint[2] + " " + toPrint[3]);
			moves.removeLast();
		}
	}

}
