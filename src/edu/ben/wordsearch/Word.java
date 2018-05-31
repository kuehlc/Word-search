package edu.ben.wordsearch;

import java.util.ArrayList;

/**
 * Creates an instance of a word to be put into the word search.
 * 
 * @author Corey Kuehl
 * @version 1.0
 */
public class Word {

	private String word;
	private String description;
	private int row1;
	private int col1;
	private int row2;
	private int col2;
	private ArrayList<Integer> coordinates;

	public Word(String word, String description) {
		this.word = word;
		this.description = description;
		coordinates = new ArrayList<>();
	}

	// Getters and Setters

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getRow1() {
		return row1;
	}

	public void setRow1(int row1) {
		this.row1 = row1;
	}

	public int getCol1() {
		return col1;
	}

	public void setCol1(int col1) {
		this.col1 = col1;
	}

	public int getRow2() {
		return row2;
	}

	public void setRow2(int row2) {
		this.row2 = row2;
	}

	public int getCol2() {
		return col2;
	}

	public void setCol2(int col2) {
		this.col2 = col2;
	}

	public ArrayList<Integer> getCoordinates() {
		return coordinates;
	}

	// Override equals and toString

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Word) {
			Word aWord = (Word) obj;
			return word.equals(aWord.getWord()) && description.equals(aWord.getDescription()) && row1 == aWord.getRow1()
					&& col1 == aWord.getCol1() && row2 == aWord.getRow2() && col2 == aWord.getCol2()
					&& coordinates.equals(aWord.getCoordinates());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Word [word=" + word + ", description=" + description + ", row1=" + row1 + ", col1=" + col1
				+ ", getWord()=" + getWord() + ", getDescription()=" + getDescription() + ", getRow1()=" + getRow1()
				+ ", getCol1()=" + getCol1() + ", getRow2()=" + getRow2() + ", getCol2()=" + getCol2() + "]";
	}
}
