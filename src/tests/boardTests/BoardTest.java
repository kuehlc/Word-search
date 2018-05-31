package tests.boardTests;

import static org.junit.Assert.*;
import org.junit.Test;
import edu.ben.wordsearch.Board;

/**
 * Tests for the Board class.
 * 
 * @author Corey Kuehl
 * @version 1.0
 */
public class BoardTest {

	/**
	 * @author Colom
	 */
	@Test
	public void testCreation() {
		String size = "small";
		String theme = "theme 1";

		Board b = new Board(size, theme);
		char[][] array = b.getBoard();

		try {
			// print array
			for (int i = 0; i < array.length; i++) {
				for (int j = 0; j < array.length; j++) {
					if (Character.isLetter(array[i][j])) {
						System.out.print(array[i][j]);
					} else {
						System.out.print(' ');
					}

				}
				System.out.println();
			}

			assertTrue(true);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * @author Corey Kuehl
	 */
	@Test
	public void testFillBoardGaps() {

		// Variables used for testing.

		String size = "small";
		String theme = "theme 1";
		boolean expected = true;
		boolean actual = true;

		// Board instance created for testing.

		Board board = new Board(size, theme);
		char[][] gameBoard = board.getBoard();

		// Check for empty spaces in the board.

		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard[i].length; j++) {
				if (gameBoard[i][j] == '\0') {
					actual = false;
					return;
				}
			}
		}

		assertEquals(expected, actual);

	}
}
