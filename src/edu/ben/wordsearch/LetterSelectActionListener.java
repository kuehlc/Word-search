package edu.ben.wordsearch;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * 
 * @author saani
 *
 */
public class LetterSelectActionListener implements ActionListener {

	private Integer startRow, startCol, endRow, endCol;
	private boolean firstLetterChosen, secondLetterChosen, isWord;
	private JButton[][] arr;
	private Board gameBoard;
	private Color colorStart;
	private JPanel foundsPanel, notFoundsPanel;
	
	/**
	 * Constructor
	 * 
	 * @param endRow <Integer> row index of current JButton - can be null
	 * @param endCol <Integer> col index of current JButton - can bee null
	 * @param arr <JButton[][]> array of JButtons to be manipulated
	 * @param gameBoard <Board> 'Backend' 
	 */
	public LetterSelectActionListener(Integer endRow, Integer endCol, JButton[][] arr, Board gameBoard) {
		// endRow and endCol = mystery button
		// startRow and startCol = this button
		
		this.endRow = endRow;
		this.endCol = endCol;
		this.arr = arr;
		this.gameBoard = gameBoard;
		firstLetterChosen = false;
		secondLetterChosen = false;
		isWord = false;
		foundsPanel = null;
		notFoundsPanel = null;
		
	}

	/**
	 * many getters and setters below 
	 */
	
	public void setFoundsPanel(JPanel founds) {
		foundsPanel = founds;
	}
	
	public void setNotFoundsPanel(JPanel notFounds) {
		notFoundsPanel = notFounds;
	}
	
	public void setStartColor(Color colorStart) {
		this.colorStart = colorStart;
	}
	
	public void setStartCoord(Integer startRow, Integer startCol) {
		this.startRow = startRow;
		this.startCol = startCol;
	}
	
	public void setEndCoord(Integer endRow, Integer endCol) {
		this.endRow = endRow;
		this.endCol = endCol;
	}
	
	public void firstLetterChosen(boolean x) {
		firstLetterChosen = x;
	}
	
	public void secondLetterChosen(boolean x) {
		secondLetterChosen = x;
	}
	
	public boolean firstChoice() {
		return firstLetterChosen;
	}
	
	public boolean secondChoice() {
		return secondLetterChosen;
	}
	
	public void isWord(boolean x) {
		isWord = x;
	}
	
	public boolean isWord() {
		return isWord;
	}
	
	
	/**
	 * @author saaniya
	 * from the interface, used when 
	 * overhead methods from validator are used to color the squares
	 * were in the process of doing it another longer way, 
	 * but it was full of bugs that didnt make sense
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// startRow and startCol is i,j coord of source button 
		
		// hold original background color values for later
		Color colorEnd = arr[endRow][endCol].getBackground();
//		Color colorStart = null;
		
		if (!firstLetterChosen) {
			// the first letter has been picked
			firstLetterChosen = true;
			
			// if it was green before, hold it in this variable
			colorStart = arr[endRow][endCol].getBackground(); 
			
			// color THIS button to reflect that it has been chosen 
			arr[endRow][endCol].setBackground(Color.YELLOW);
			arr[endRow][endCol].setContentAreaFilled(true);
			
			
			// set this value true in all AL across the board
			for (int i = 0; i < arr.length; i++) {
				for (int j = 0; j < arr[i].length; j++) {
					
					// there is only 1 AL on every button!
					LetterSelectActionListener aL = (LetterSelectActionListener) 
							arr[i][j].getActionListeners()[0];
					aL.setStartCoord(endRow, endCol);
					aL.setStartColor(colorStart);
					aL.firstLetterChosen(true);
					
					// every button already has an end coord (itself)
					// set THIS coord as the start coord for every other button on array
					// every button knows that a first buttons been chosen

				}
			}
			
		} else if (firstLetterChosen && startRow != null && startCol != null) {

//			colorStart = arr[startRow][startCol].getBackground();
			secondLetterChosen(true);
			
			// this should not evaluate if the first if cond is true;
			// the first letter = startpoint
			// the second letter (just clicked now) = endpoint
			
			// determine if it is a word 
			// set the class level boolean 'isWord' true if the user found a correct word
			// next course of action is decided based on this boolean value
			
			// this method determines if the user found a word or not
			isWord(Validator.isMatch(gameBoard.getBoard(), gameBoard.getWords(), 
					endRow, endCol, startRow, startCol));
			
			
		}
		
		if (secondLetterChosen) {
			
			if (isWord) {
				System.out.println("correct");
				
				// highlight all tiles in between start and end row/cols
				
				// account for directionality by storing the smaller value of each coord pair
				// and incrementing to the bigger value
				// increment for each will vary depending on horz/vert/diag orientation of the word
				
//				int firstValRow = Math.min(startRow, endRow);
//				int lastValRow = Math.max(startRow, startCol);
//				
//				int firstValCol = Math.min(startCol, endCol);
//				int lastValCol = Math.max(startCol, endCol);
				
				Word word = Validator.getMatchingWord(gameBoard.getWords(), endRow, endCol, startRow, startCol);
				
				if (word != null) {
					ArrayList<Integer> coords = word.getCoordinates();
					// move to found list here;
					gameBoard.setFoundWord(word);
					
					for (int i = 0; i < coords.size(); i++) {	
						int[] wordCoords = Validator.intToCoord(coords.get(i), 
								gameBoard.getBoard()[0].length);
						int row = wordCoords[0];
						int col = wordCoords[1];
						arr[row][col].setContentAreaFilled(true);
						arr[row][col].setBackground(Color.GREEN);
						
					}
					
					JTextArea foundWords = (JTextArea) foundsPanel.getComponents()[0];
					foundWords.setText("Words Found:\n" + gameBoard.foundWords());
					foundsPanel.repaint();
					
					JTextArea notFoundWords = (JTextArea) notFoundsPanel.getComponents()[0];
					notFoundWords.setText("Words to Find:\n" + gameBoard.notFoundWords());
					notFoundsPanel.repaint();
					
					// win condtion
					if (gameBoard.isWinner()) {
						// popup menu that informs the player that they won!
						JOptionPane.showMessageDialog(new JFrame(), "Congratulations, you found all the words!");
						System.exit(0);
					}
				}
				
//              tried to color each button by taking the start and end button
				// and figure out
//				
//				if (startRow == endRow) {
//					// word is horizontal, check only col for coloring
////					rowIncr = 0;
////					colIncr = 1;
//					System.out.println("horizontal word");
//					
//					for (int i = firstValCol; i <= lastValCol; i++) {
//						arr[startRow][i].setBackground(Color.green);
//						arr[startRow][i].setContentAreaFilled(true);
//					}
//					
//					
//				} else if (startCol == endCol) {
//					// word is vertical, check only row for coloring
////					rowIncr = 1;
////					colIncr = 0;
//					System.out.println("vertical word");
//					
//					for (int i = firstValRow; i <= lastValRow; i++) {
//						arr[i][startCol].setBackground(Color.green);
//						arr[i][startCol].setContentAreaFilled(true);
//					}
//					
//					
//				} else if (startRow > endRow && startCol < endCol) {
//					
//					// word is diagonal, check both row and col for coloring 
////					// word is lower left to upper right
////					rowIncr = 1;
////					colIncr = 1;
//					System.out.println("diagonal word lower left to upper right");
//					
//					for (int i = endRow; i <= startRow;) {
//						for (int j = endCol; j <= startCol;) {
//							arr[i][j].setBackground(Color.green);
//							arr[i][j].setContentAreaFilled(true);
//							i++; j--;
//						}
//					}
//					
//				} else if (startRow < endRow && startCol > endCol) {
//					
//					for (int i = startRow; i <= endRow;) {
//						for (int j = startCol; j <= endCol;) {
//							arr[i][j].setBackground(Color.green);
//							arr[i][j].setContentAreaFilled(true);
//							i++; j++;
//						}
//					}
//					
//					System.out.println("diagonal word upper left to lower right");
//				}
//				
				
				
				// was trying to write one loop to execute all 3
//				for (int i = firstValRow; i < lastValRow; i = i + rowIncr) {
//					for (int j = firstValCol; j < lastValCol; j = j + colIncr) {
//						arr[i][j].setContentAreaFilled(true);
//						arr[i][j].setBackground(Color.green);
//					}
//				}
				
			
			} else {
				
				
				// reset tile colors if incorrect
				System.out.println("incorrect");

				arr[startRow][startCol].setContentAreaFilled(false);
				arr[endRow][endCol].setContentAreaFilled(false);
				
				System.out.println("colorStart = " + colorStart);
				System.out.println("colorEnd = " + colorEnd);
				
				if (colorStart.equals(Color.GREEN)) {
					arr[startRow][startCol].setContentAreaFilled(true);
					arr[startRow][startCol].setBackground(colorStart);
					System.out.println("one tile is green (start)");
				}
				
				if (colorEnd.equals(Color.GREEN)) {
					arr[endRow][endCol].setContentAreaFilled(true);
					arr[endRow][endCol].setBackground(colorEnd);
					System.out.println("one tile is green (end)");
				}
				
			}
			
			// reset boolean values
			for (int i = 0; i < arr.length; i++) {
				for (int j = 0; j < arr[i].length; j++) {
					
					// there is only 1 AL on every button!
					LetterSelectActionListener aL = (LetterSelectActionListener) 
							arr[i][j].getActionListeners()[0];
					aL.setStartCoord(null, null);
					aL.firstLetterChosen(false);
					aL.secondLetterChosen(false);
					aL.isWord(false);
					aL.setStartColor(null);
				}
			}
			
			
		}
	}
	
//	@Override
//	public void actionPerformed(ActionEvent arg0) {
//		// a button is pressed on the array
//		// each tile knows its row, col, position 
//		
//		// highlight this tile yellow to indicate 
//		// to the user that a first selection has been made
//		arr[startRow][startCol].setContentAreaFilled(true);
//		arr[startRow][startCol].setBackground(Color.YELLOW);
//		
//		// add another action listener to all buttons
//		// all other buttons should know about this button! 
//		
//		for (int i = 0; i < arr.length; i++) {
//			for (int j = 0; j < arr[i].length; j++) {
//				
//				// remove other AL from the board, to not confuse during button press
//				// remove LetterSelectAL and add WordSelectAL to all buttons
//				arr[i][j].removeActionListener(this);
//				WordSelectActionListener aL = new WordSelectActionListener(startRow, startCol, i, j, arr);
//				arr[i][j].addActionListener(aL);
//				
//				// add WordSelectAL
//			}
//		}
//		
//	}

}
