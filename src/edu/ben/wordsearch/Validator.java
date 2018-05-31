package edu.ben.wordsearch;

import java.util.ArrayList;

/**
 * Provides validation logic to the game. Allows for Word objects to be matched
 * to coordinate values, or for a Word ArrayList to be searched to see if it
 * contains a Word with the matching coordinate values.
 * 
 * @author Colom Boyle
 * @version 1
 *
 */
public class Validator {

	/**
	 * Constructor
	 */
	public Validator() {

	}

	/**
	 * Determines if a Word in the passed in Word ArrayList matches the passed in
	 * row/col coordinate values.
	 * 
	 * @param board
	 *            A char array.
	 * @param words
	 *            An ArrayList of Words.
	 * @param row1
	 *            A row value.
	 * @param col1
	 *            A column value.
	 * @param row2
	 *            A row value.
	 * @param col2
	 *            A column value.
	 * @return Returns true if a word is found in the Word ArrayList that matches
	 *         the row/col pairs (or the pairs swapped), else false.
	 */
	public static boolean isMatch(char[][] board, ArrayList<Word> words, int row1, int col1, int row2, int col2) {

		if (isInsideBounds(board, row1, col1, row2, col2)) {
			// loop through word arraylist
			for (int i = 0; i < words.size(); i++) {

				int startRow = words.get(i).getRow1();
				int startCol = words.get(i).getCol1();
				int endRow = words.get(i).getRow2();
				int endCol = words.get(i).getCol2();

				// determines if the passed in coordinate values match the current word's
				// coordinate values
				if (isCoordinateMatch(row1, startRow, col1, startCol, row2, endRow, col2, endCol)
						|| isCoordinateMatch(row2, startRow, col2, startCol, row1, endRow, col1, endCol)) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	/**
	 * Allows for a Word to be matched by its corresponding row1, col1, row2, col2
	 * values.
	 * 
	 * @author Colom Boyle
	 * 
	 * @param words
	 *            An ArrayList of Words.
	 * @param row1
	 *            A row value.
	 * @param col1
	 *            A column value.
	 * @param row2
	 *            A row value.
	 * @param col2
	 *            A column value.
	 * @return Returns a Word that matches the row and column pairs passed into the
	 *         method. Returns null if no matching word is found.
	 */
	public static Word getMatchingWord(ArrayList<Word> words, int row1, int col1, int row2, int col2) {
		for (int i = 0; i < words.size(); i++) {

			int startRow = words.get(i).getRow1();
			int startCol = words.get(i).getCol1();
			int endRow = words.get(i).getRow2();
			int endCol = words.get(i).getCol2();

			if (isCoordinateMatch(row1, startRow, col1, startCol, row2, endRow, col2, endCol)
					|| isCoordinateMatch(row2, startRow, col2, startCol, row1, endRow, col1, endCol)) {
				return words.get(i);
			}
		}
		return null;
	}

	/**
	 * Determines if the passed in row and column values match their comparison
	 * parameters.
	 * 
	 * @author Colom Boyle
	 * 
	 * @param row1
	 *            A row value.
	 * @param compRow1
	 *            A row value to be compared to row1.
	 * @param col1
	 *            A column value.
	 * @param compCol1
	 *            A column value to be compared to col1.
	 * @param row2
	 *            A row value.
	 * @param compRow2
	 *            A row value to be compared to row2.
	 * @param col2
	 *            A column value.
	 * @param compCol2
	 *            A column value to be compared to col2.
	 * @return Returns true if all row and column values match their corresponding
	 *         compRow/compCol values, else false.
	 */
	private static boolean isCoordinateMatch(int row1, int compRow1, int col1, int compCol1, int row2, int compRow2,
			int col2, int compCol2) {
		return row1 == compRow1 && col1 == compCol1 && row2 == compRow2 && col2 == compCol2;
	}

	/**
	 * Determines if passed in coordinates are within the passed in array's bounds.
	 * 
	 * @author Colom Boyle
	 * 
	 * @param board
	 *            A char array.
	 * @param row1
	 *            A row value.
	 * @param col1
	 *            A column value.
	 * @param row2
	 *            A row value.
	 * @param col2
	 *            A column value.
	 * @return Returns true if all row and column coordinates are within array
	 *         bounds, else false.
	 */
	private static boolean isInsideBounds(char[][] board, int row1, int col1, int row2, int col2) {
		return row1 >= 0 && row1 < board.length && col1 >= 0 && col1 < board[0].length && row2 >= 0
				&& row2 < board.length && col2 >= 0 && col2 < board[0].length;
	}

	// /**
	// * Determines if start and end coordinates allow for a valid horizontal,
	// * vertical, or diagonal search, and that the search distance is the same as
	// the
	// * passed in word's length. (i.e. an irregular search path is not present and
	// * the path length matches the number of characters in the word).
	// *
	// * @author Colom Boyle
	// *
	// * @param board
	// * A char array.
	// * @param word
	// * A String representing a word.
	// * @param row1
	// * Starting row value.
	// * @param col1
	// * Starting column value.
	// * @param row2
	// * Ending row value.
	// * @param col2
	// * Ending column value.
	// * @return Returns true if both sets of coordinates allow for a valid search
	// * (horizontal, vertical, or diagonal) and the word length matches the
	// * coordinate search length, else false.
	// */
	// private static boolean isCoordinateSetValid(char[][] board, String word, int
	// row1, int col1, int row2, int col2) {
	//
	// int rowDifference = Math.abs(row1 - row2);
	// int colDifference = Math.abs(col1 - col2);
	// int lengthCheck = word.length() - 1;
	//
	// // verify at least one side matches word length and search pattern is either
	// // horizontal, vertical, or diagonal
	// return (rowDifference == lengthCheck || colDifference == lengthCheck)
	// && ((rowDifference == 0 || colDifference == 0) || (rowDifference ==
	// colDifference));
	// }

	/**
	 * Converts row and column values into a single int value.
	 * 
	 * @author Colom Boyle
	 * 
	 * @param row
	 *            A row value.
	 * @param col
	 *            A column value.
	 * @param numCols
	 *            The number of cols in the array.
	 * @return Returns an int value representing the row and column values. Value is
	 *         determined by row * numCols + col
	 */
	public static int coordToInt(int row, int col, int numCols) {
		return row * numCols + col;
	}

	/**
	 * Converts a single int value into array row and column values. Returns values
	 * in an array.
	 * 
	 * @author Colom Boyle
	 * 
	 * @param value
	 *            An int coordinate value.
	 * @param numCols
	 *            The number of columns in the array.
	 * @return Returns the row and column values as coordinates in an array. Index 0
	 *         represents the row value, index 1 represents the column value.
	 */
	public static int[] intToCoord(int value, int numCols) {
		int[] coord = new int[2];
		coord[0] = value / numCols;
		coord[1] = value % numCols;
		return coord;
	}
}
