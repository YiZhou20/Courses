
/**
 * CS61BL Project3 Sliding-Block Puzzle Solver
 * 
 * @author Anuli Kshetramade (og)
 * @author Rohan Sachdev (dy)
 * @author Spencer Axelrod (do)
 * @author Yi Zhou (ds)
 */

/**
 * This class contains methods that support the main execution in Solver.java
 * @param parent Tray object from which _this_ is generated, null for initial configuration
 * @param blocks ArrayList of int[4] representing upper-left, lower-right corner coordinates of blocks in input order
 * @param emptyArea size of emptyspace of a puzzle tray, used for checking overlapping
 * @param tray boolean matrix with cells occupied by block as true, elsewhere false
 * @param index tracemark of input position of blocks
 * @param posMoves boolean matrix of 4 columns corresponding to blocks' move up, down, left, right
 * @param myMove Array of 4 representing the coordinates of upper-left corners for parent and _this_, initially null
 */

import java.util.*;

public class Tray {
	private Tray parent;
	private ArrayList<int []> blocks; // ArrayList of arrays
	private int emptyArea;
	private boolean[][] tray; // matrix of block occupation
	private int index;
	private boolean[][] posMoves;
	private int[] myMove;
	private int dist;
	private int blockPos;
	private int blockDir;
	
	public Tray(String size) {
		parent = null;
		blocks = new ArrayList<int[]> ();
		Scanner s = new Scanner(size);
		int m = s.nextInt();
		int n = s.nextInt();
		tray = new boolean[m][n];
		emptyArea = m*n;
		index = -1;
	}
	
	// construct Tray with specified tray and parent
	public Tray(Tray tr, Tray par) {
		parent = par;
		blocks = new ArrayList<int[]> ();
		Iterator<int[]> iter = tr.blockIterator();
		while (iter.hasNext()) {
			int[] next = iter.next();
			int[] add = new int[4];
			for (int i = 0; i < 4; i++) {
				add[i] = next[i];
			}
			blocks.add(add);
		}
		boolean[][] trOcc = tr.getTray();
		int l = trOcc.length;
		int w = trOcc[0].length;
		tray = new boolean[l][w];
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < w; j++) {
				tray[i][j] = trOcc[i][j];
			}
		}
		emptyArea = tr.getEmptyArea();
		index = tr.getBlocks().size();
	}
	
	public void addBlock(String line) {
		Scanner s = new Scanner(line);
		int[] coords = new int[4];
		coords[0] = s.nextInt();
		coords[1] = s.nextInt();
		coords[2] = s.nextInt();
		coords[3] = s.nextInt();
		index = index + 1;
		blocks.add(index, coords);
	}
	
	// mark grid cells occupied by blocks as true
	public void markOccupied(String line) {
		Scanner s = new Scanner(line);
		int[] coords = new int[4];
		coords[0] = s.nextInt();
		coords[1] = s.nextInt();
		coords[2] = s.nextInt();
		coords[3] = s.nextInt();
		for (int i = coords[0]; i < coords[2]+1; i++) {
			for (int j = coords[1]; j < coords[3]+1; j++) {
				tray[i][j] = true;
				emptyArea--;
			}
		}
	}
	
	// pos: index of the block to be moved in ArrayList blocks
	// dir: 0 -- move up; 1 -- move down; 2 -- move left; 3 -- move right
	public Tray moveBlock(int pos, int dir) {
		Tray toRtn = new Tray(this,this);
		toRtn.myMove = new int[4];
		int n1 = toRtn.blocks.get(pos)[0];
		int n2 = toRtn.blocks.get(pos)[1];
		int n3 = toRtn.blocks.get(pos)[2];
		int n4 = toRtn.blocks.get(pos)[3];
		toRtn.myMove[0] = n1;
		toRtn.myMove[1] = n2;
		switch (dir) {
		case 0:
			for (int i = n2; i <= n4; i++) {
				toRtn.tray[n3][i] = false;
				toRtn.emptyArea++;
			}
			n1--;
			n3--;
			toRtn.blocks.get(pos)[0] -= 1;
			toRtn.blocks.get(pos)[2] -= 1;
			for (int i = n2; i <= n4; i++) {
				toRtn.tray[n1][i] = true;
				toRtn.emptyArea--;
			}
			break;
		case 1:
			for (int i = n2; i <= n4; i++) {
				toRtn.tray[n1][i] = false;
				toRtn.emptyArea++;
			}
			n1++;
			n3++;
			toRtn.blocks.get(pos)[0] += 1;
			toRtn.blocks.get(pos)[2] += 1;
			for (int i = n2; i <= n4; i++) {
				toRtn.tray[n3][i] = true;
				toRtn.emptyArea--;
			}
			break;
		case 2:
			for (int i = n1; i <= n3; i++) {
				toRtn.tray[i][n4] = false;
				toRtn.emptyArea++;
			}
			n2--;
			n4--;
			toRtn.blocks.get(pos)[1] -= 1;
			toRtn.blocks.get(pos)[3] -= 1;
			for (int i = n1; i <= n3; i++) {
				toRtn.tray[i][n2] = true;
				toRtn.emptyArea--;
			}
			break;
		case 3:
			for (int i = n1; i <= n3; i++) {
				toRtn.tray[i][n2] = false;
				toRtn.emptyArea++;
			}
			n2++;
			n4++;
			toRtn.blocks.get(pos)[1] += 1;
			toRtn.blocks.get(pos)[3] += 1;
			for (int i = n1; i <= n3; i++) {
				toRtn.tray[i][n4] = true;
				toRtn.emptyArea--;
			}
			break;
		}
		toRtn.myMove[2] = n1;
		toRtn.myMove[3] = n2;
		toRtn.blockPos = pos+1;
		toRtn.blockDir = dir;
		return toRtn;
	}
	
	// get track of moves to _this_
	public LinkedList<int []> getTrack() {
		LinkedList<int []> track = new LinkedList<int []> ();
		for (Tray tray = this;  tray.parent != null; tray = tray.parent) {
			track.add(tray.myMove);
		}
		return track;
	}
	
	public boolean equals(Object toCompare) {
		ArrayList<int []> toCom = ((Tray) toCompare).getBlocks();
		if (blocks.size() != toCom.size()) {
			throw new IllegalStateException("Number of blocks doesn't match");
		} else {
			int counts = 0;
			for (int i = 0; i < blocks.size(); i++) {
				int[] ar = blocks.get(i);
				for (int j = 0; j < toCom.size(); j++) {
					boolean foundMatch = true;
					for (int j2 = 0; j2 < 4; j2++) {
						if (toCom.get(j)[j2] != ar[j2]) {
							foundMatch = false;
							break;
						}
					}
					if (foundMatch) {
						counts++;
						break;
					}
				}
			}
			if (counts != blocks.size()) {
				return false;
			} else {
			return true;
			}
		}
	}
	
	public int hashCode() {
		int hashVal = 0;
		int addOn = 0;
		Iterator<int[]> iterator = blockIterator();
		while(iterator.hasNext()) {
			int[] nextAr = iterator.next();
			hashVal = hashVal + addOn + nextAr[0] + nextAr[1] * 2 + nextAr[2] * 3 + nextAr[3] * 4;
			addOn++;
		}
		return hashVal;
	}
	
	public boolean isComplete(Tray goal) {
		ArrayList<int []> goalReq = goal.getBlocks();
		int counts = 0;
		for (int i = 0; i < goalReq.size(); i++) {
			int[] ar = goalReq.get(i);
			for (int j = 0; j < blocks.size(); j++) {
				boolean foundMatch = true;
				for (int j2 = 0; j2 < 4; j2++) {
					if (blocks.get(j)[j2] != ar[j2]) {
						foundMatch = false;
						break;
					}
				}
				if (foundMatch) {
					counts++;
					break;
				}
			}
		}
		if (counts != goalReq.size()) {
			return false;
		} else {
		return true;
		}
	}
	
	// ArrayList of indices of all occurrences of each block specified in goal in _this_
	public ArrayList<ArrayList<Integer>> goalBlocks (Tray goal) {
		ArrayList<ArrayList<Integer>> indexOfGoal = new ArrayList<ArrayList<Integer>>();
		ArrayList<int []> goalReq = goal.getBlocks();
		for (int i = 0; i < goalReq.size(); i++) {
			ArrayList<Integer> indices = new ArrayList<Integer> ();
			int[] goalCoords = goalReq.get(i);
			for (int j = 0; j < blocks.size(); j++) {
				int[] trayCoords = blocks.get(j);
				if (trayCoords[2] - trayCoords[0] == goalCoords[2] - goalCoords[0] &&
						trayCoords[1] - trayCoords[3] == goalCoords[1] - goalCoords[3]) {
					indices.add(j);
				}
			}
			indexOfGoal.add(indices);
		}
		return indexOfGoal;
	}
	
	// heuristic
	// total shortest distance of each block in _this_ compared to goal, 0 for blocks not specified
	// idea from Manhattan distance -- comparing upper-left corner with corresponding goal block
	public int getDist (Tray goal) {
		ArrayList<ArrayList<Integer>> indices = this.goalBlocks(goal);
		ArrayList<int[]> goalB = goal.getBlocks();
		dist = 0;
		for (int i = 0; i < goalB.size(); i++) {
			int cellDist;
			int toRm = 0;
			cellDist = Math.abs(blocks.get(indices.get(i).get(0))[0] - goalB.get(i)[0]) +
					Math.abs(blocks.get(indices.get(i).get(0))[1] - goalB.get(i)[1]);
			for (int j = 1; j < indices.get(i).size(); j++) {
				int altDist;
				altDist = Math.abs(blocks.get(indices.get(i).get(j))[0] - goalB.get(i)[0]) +
						Math.abs(blocks.get(indices.get(i).get(j))[1] - goalB.get(i)[1]);
				if (altDist < cellDist) {
					cellDist = altDist;
					toRm = j;
				}
			}
			dist += cellDist;
			for (int j = i + 1; j < goalB.size(); j++) {
				for (int j2 = 0; j2 < indices.get(j).size(); j2++) {
					if (indices.get(j).get(j2) == indices.get(i).get(toRm)) {
						indices.get(j).remove(j2);
						break;
					}
				}
			}
		}
		return dist;
	}
	
	// Depth-First for easy puzzles, Breadth-First might be faster for harder ones
	// Not considering moves for more than one space for now since shortest solution not required
	// Might adjust later to trade memory for efficiency
	public boolean [][] possibleMoves() {
		// up, down, left, right
		int size = blocks.size();
		int index = -1;
		posMoves = new boolean [size][4];
		Iterator<int []> blockIter = blockIterator();
		while (blockIter.hasNext()) {
			int[] coords = blockIter.next();
			boolean[] canMove = new boolean[4];
			if (coords[0] != 0) {
				canMove[0] = true;
				for (int i = coords[1]; i <= coords[3]; i++) {
					if (tray[coords[0]-1][i]) {
						canMove[0] = false; // Cannot move up
						break;
					}
				}
			}
			if (coords[2] != tray.length - 1) {
				canMove[1] = true;
				for (int i = coords[1]; i <= coords[3]; i++) {
					if (tray[coords[2]+1][i]) {
						canMove[1] = false; // Cannot move down
						break;
					}
				}
			}
			if (coords[1] != 0) {
				canMove[2] = true;
				for (int i = coords[0]; i <= coords[2]; i++) {
					if (tray[i][coords[1]-1]) {
						canMove[2] = false; // Cannot move left
						break;
					}
				}
			}
			if (coords[3] != tray[0].length - 1) {
				canMove[3] = true;
				for (int i = coords[0]; i <= coords[2]; i++) {
					if (tray[i][coords[3]+1]) {
						canMove[3] = false; // Cannot move right
					}
				}
			}
			index = index+1;
			posMoves[index] = canMove;
		}
		return posMoves;
	}
	
	public Iterator<int[]> blockIterator () {
		return blocks.iterator();
	}
	
	// check each time with its parent tray, there should only be one element difference
	// the element that differs should differ in either row indices or column indices by same amount
	public void isOK() {
		if (parent != null) {
			if (blocks.size() != parent.blocks.size()) {
				throw new IllegalStateException("Number of blocks doesn't match");
			} else if (emptyArea != parent.emptyArea) {
				throw new IllegalStateException("Overlapping detected");
			} else {
				boolean oneDiff = false;
				for (int i = 0; i < blocks.size(); i++) {
					int[] curCoords = blocks.get(i);
					int[] parCoords = parent.blocks.get(i);
					if (curCoords[0] != parCoords[0] || curCoords[1] != parCoords[1] ||
							curCoords[2] != parCoords[2] || curCoords[3] != parCoords[3]) {
						if (oneDiff) {
							throw new IllegalStateException("More than one block changed during single move");
						} else {
							if (curCoords[0] != parCoords[0]) {
								if (curCoords[1] != parCoords[1] || curCoords[3] != parCoords[3]) {
									throw new IllegalStateException("Block seem to be silded both horizontally and vertically");
								} else if (curCoords[2] - parCoords[2] != curCoords[0] - parCoords[0] ||
										curCoords[0] - parCoords[0] != myMove[2] - myMove[0]) {
									throw new IllegalStateException("Moves of up and down side don't match");
								}
							}
							if (curCoords[1] != parCoords[1]) {
								if (curCoords[0] != parCoords[0] || curCoords[2] != parCoords[2]) {
									throw new IllegalStateException("Block seem to be silded both horizontally and vertically");
								} else if (curCoords[3] - parCoords[3] != curCoords[1] - parCoords[1] ||
										curCoords[1] - parCoords[1] != myMove[3] - myMove[1]) {
									throw new IllegalStateException("Moves of left and right side don't match");
								}
							}
							oneDiff = true;
						}
					}
				}
			}
		}
		
	}
	
	// getters mainly for JUnit tests
	public ArrayList<int []> getBlocks() {
		return blocks;
	}
	
	public boolean[][] getTray() {
		return tray;
	}
	
	public boolean[][] getPosMoves () {
		return posMoves;
	}
	
	public int getEmptyArea() {
		return emptyArea;
	}
	
	// helpful getters for debugging mode
	public int getDistance() {
		return dist;
	}
	
	public int getBlockPos() {
		return blockPos;
	}
	
	public String getBlockDir() {
		String moveDir = new String();
		switch (blockDir) {
		case 0:
			moveDir = new String("up");
			break;
		case 1:
			moveDir = new String("down");
			break;
		case 2:
			moveDir = new String("left");
			break;
		case 3:
			moveDir = new String("right");
			break;
		}
		return moveDir;
	}
}