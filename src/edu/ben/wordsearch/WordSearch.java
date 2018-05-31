package edu.ben.wordsearch;

import java.awt.EventQueue;

/**
 * 
 * @author Joe Warhurst
 * @version 1
 *
 */
public class WordSearch {

	public static void main(String[] args) {
		//Creates instance of the game and begins the game
		WordSearch game = new WordSearch();
		game.playGame();

	}
	/**
	 * Method to start the game and play
	 */
	public void playGame() {

		EventQueue.invokeLater(new Runnable() {
			/**
			 * Presents the option to choose size and theme and creates the board by starting the board and GUI
			 */
			public void run() {
				try {
					String size = WordSearchGUI.chooseSize();
					String theme = WordSearchGUI.chooseTheme();
					//if check to make sure the user selected a size and a theme before starting
					if (size != null && theme != null) {
						Board gameBoard = new Board(size, theme);
						WordSearchGUI gameFrame = new WordSearchGUI(gameBoard);
						gameFrame.setVisible(true);
						
						
					}
				} catch (Exception e) {

				}
			}
		});
	}
}
