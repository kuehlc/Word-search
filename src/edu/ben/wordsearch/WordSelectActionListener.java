package edu.ben.wordsearch;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class WordSelectActionListener implements ActionListener {

	
	private int row, startRow, col, startCol;
	private JButton[][] arr;
	
	public WordSelectActionListener(int startRow, int startCol, int row, int col, JButton[][] arr) {
		
		this.row = row;
		this.col = col;
		this.startRow = startRow;
		this.startCol = startCol;
		
		// knows its own row col and the row col of the first pressed button 
		// all available JButtons need methods in the board class to validate word selection! 
		// once word has been chosen (whether valid or not) 
		// we need to remove WordSelectActionListener from all Jbuttons
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// hold original background color values for later
		Color orig1 = arr[startRow][startCol].getBackground();
		Color orig2 = arr[row][col].getBackground();
		
		// call back end methods to validate if the chosen word 
		// exists in the word bank or no
		// pass on start and end row/col 
		
		// if user selects a correct word,
		// highlight all tiles in between start and end row/cols
		
		if (startRow == row) {
			// word is horizontal, check only col for coloring
		} else if (startCol == col) {
			// word is vertical, check only row for coloring
		} else {
			// word is diagonal, check both row and col for coloring 
		}
		
		// update the JPanels to reflect the change in users word bank
		
		// if the user selects an incorrect word
		// no changes in word bank, but let the user know 
		// that they selected something wrong
		
		// then remove all instances of WordSelectAL for this instance of chosen word
		
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				
				ActionListener[] aL = arr[i][j].getActionListeners();
				if (aL != null && aL.length > 0) {
					arr[i][j].removeActionListener(aL[aL.length-1]);
					// add letter select aL
					arr[i][j].setContentAreaFilled(false);
				}
				
			}
		}
		

		// reset back to original colors after button click 
		// this is important if user clicked a button that was colored green, 
		// expecting an intersecting word
		
		if (orig1.equals(Color.green)) {
			arr[startRow][startCol].setContentAreaFilled(true);
		}
		
		if (orig2.equals(Color.GREEN)) {
			arr[row][col].setContentAreaFilled(true);
		}
		arr[startRow][startCol].setBackground(orig1);
		arr[row][col].setBackground(orig2);
		
	}

}
