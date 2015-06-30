

import java.util.ArrayList;

import junit.framework.TestCase;

public class TrayTest extends TestCase{
	
	public void testContructor(){
		String size = "5  4  ";
		Tray t = new Tray(size);
		boolean [][] test = t.getTray();
		assertEquals(test.length,5);
		// check if each rows have 4 cols or not.
		int totColsCal = 0;
		for(int i = 0;i < test.length;i++){
			totColsCal = totColsCal + test[i].length;  // this is to test if total columns should given number of columns * number of rows.
		}
		int totalCols = totColsCal/test.length;
		assertEquals(totalCols,4);
	}
	public void testAddBlock(){
		String size = "5  4  ";
		Tray t = new Tray(size);
		String line  = new String("0 2 3 3 ");
		String line2 = new String("0 0 1 1 ");
		t.addBlock(line);
		t.addBlock(line2);
		ArrayList<int []> blockCopy;
		blockCopy = t.getBlocks();
		assertEquals(blockCopy.get(0)[0],0);
		assertEquals(blockCopy.get(0)[1],2);
		assertEquals(blockCopy.get(0)[2],3);
		assertEquals(blockCopy.get(0)[3],3);
		// testing if it adds two blocks correctly to the tray.
		assertEquals(blockCopy.get(1)[0],0);
		assertEquals(blockCopy.get(1)[1],0);
		assertEquals(blockCopy.get(1)[2],1);
		assertEquals(blockCopy.get(1)[3],1);
		assertTrue(t.isComplete(t));	// since we gave goal tray as t itself, it should be complete.	
		
	}
	public void testMarkOccupied(){
		String size = "5  4  ";
		Tray t = new Tray(size);
		String line  = new String("0 2 3 3 ");
		t.addBlock(line);
		t.markOccupied(line);
	    int EmptyArea;
		EmptyArea = t.getEmptyArea();
		assertEquals(EmptyArea,12);
		assertTrue(t.isComplete(t));
	}
	public void testEquals(){
		String size = "5  4  ";
		Tray t = new Tray(size);
		String line1  = new String("0 2 3 3 ");
		String line2 = new String("0 0 1 1 ");
		t.addBlock(line1);
		t.markOccupied(line1);
		t.addBlock(line2);
		t.markOccupied(line2);
		String size1 = "5  4  ";
		Tray copy = new Tray(size1);
		String line3  = new String("0 2 3 3 ");
		String line4 = new String("0 0 1 1 ");
		copy.addBlock(line3);
		copy.markOccupied(line3);
		copy.addBlock(line4);
		copy.markOccupied(line4);
		assertTrue(t.equals(copy));
		assertEquals(t,copy);
		assertEquals(t.hashCode(),copy.hashCode());
	}
}
