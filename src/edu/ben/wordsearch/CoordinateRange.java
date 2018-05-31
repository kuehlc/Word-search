package edu.ben.wordsearch;

import java.util.ArrayList;

public class CoordinateRange {

	// private int row1;
	// private int col1;
	// private int row2;
	// private int col2;
	// private int orientation;
	private ArrayList<Integer> coordinates;

	public CoordinateRange() {
		// this.orientation = orientation;
		coordinates = new ArrayList<>();
	}

	public int getSize() {
		// if (orientation == Board.HORIZONTAL) {
		// return Math.abs(col1 - col2) + 1;
		// }
		// return Math.abs(row1 - row2) + 1;
		return coordinates.size();
	}

	// public int getRow1() {
	// return row1;
	// }
	//
	// public void setRow1(int row1) {
	// this.row1 = row1;
	// }
	//
	// public int getCol1() {
	// return col1;
	// }
	//
	// public void setCol1(int col1) {
	// this.col1 = col1;
	// }
	//
	// public int getRow2() {
	// return row2;
	// }
	//
	// public void setRow2(int row2) {
	// this.row2 = row2;
	// }
	//
	// public int getCol2() {
	// return col2;
	// }
	//
	// public void setCol2(int col2) {
	// this.col2 = col2;
	// }

//	public int getOrientation() {
//		return orientation;
//	}
//
//	public void setOrientation(int orientation) {
//		this.orientation = orientation;
//	}

	public ArrayList<Integer> getCoordinates() {
		return coordinates;
	}

	@Override
	public String toString() {
		// return "(row1: " + row1 + ", col1: " + col1 + ", row2: " + row2 + ", col2: "
		// + col2 + ", orientation: "
		// + orientation + ", size: " + getSize() + ")";
		return "(start: " + coordinates.get(0) + ", end: " + coordinates.get(coordinates.size() - 1)
				+ /*
					 * ", orientation: " + orientation +
					 */", size: " + getSize() + ")";
	}

}
