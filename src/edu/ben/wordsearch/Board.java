package edu.ben.wordsearch;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Creates the board for the word search.
 * 
 * @author Corey Kuehl
 * @version 1.0
 */
public class Board {

	private static char[][] board;

	private final int small = 10;
	private final int medium = 20;
	private final int large = 30;

	private final int wordMaxSmall = 5;
	private final int wordMaxMedium = 7;
	private final int wordMaxLarge = 10;
	private String size;
	private String theme;
	
	private static ArrayList<Word> words;
	private ArrayList<Word> notFoundWords;
	private ArrayList<Word> foundWords;

	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;
	public static final int DIAGONAL_UP = 2;
	public static final int DIAGONAL_DOWN = 3;
	

	/**
	 * Creates an instance of the Board.
	 * 
	 * @param size
	 *            is the size of the the board to be created chosen by the user.
	 * @param theme
	 *            is the theme of the board chosen by the user.
	 */
	public Board(String size, String theme) {

		words = new ArrayList<Word>();
		foundWords = new ArrayList<Word>();
		notFoundWords = new ArrayList<Word>();
		
		this.size = size;
		this.theme = theme;
		
		createBoard(size, theme);
		setRandomCoordinates();
		addWordsToBoard();
		assignWordRowsAndCols();
		fillBoardGaps();

	}

	/**
	 * @author saaniya
	 * @return true if the game is finished (no more words to be found)
	 */
	public boolean isWinner() {
		return notFoundWords.size() == 0 && foundWords.size() == words.size();
	}

	/**
	 * Reads the words in from the theme file.
	 * 
	 * @author Corey Kuehl
	 * @param theme
	 *            is the file that the user chose for the group of words to be used
	 *            in the word search.
	 * @return the array list of words from the theme file.
	 */
	public ArrayList<String> getFileInfo(String theme) {

		Scanner readFile = null;
		File themeFile = new File(theme + ".txt");
		ArrayList<String> lineFromFile = null;

		if (themeFile.exists() && themeFile.canRead()) {

			try {

				readFile = new Scanner(themeFile);
				lineFromFile = new ArrayList<String>();

				while (readFile.hasNextLine()) {

					lineFromFile.add(readFile.nextLine());

				}

				return lineFromFile;

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (readFile != null) {
					readFile.close();
				}
			}
		}

		return lineFromFile;
	}

	/**
	 * Reads in the words from the theme file, then randomly selects a list of words
	 * to be used for the word search.
	 * 
	 * @author Corey Kuehl
	 * @param theme
	 *            is the file of words chosen by the user.
	 * @param wordCount
	 *            is the amount of words needed to be found in the word search.
	 * @return the list of randomly selected words from the file as an ArrayList.
	 */
	public ArrayList<Word> getRandomWordList(String theme, int wordCount) {

		ArrayList<String> fileList = getFileInfo(theme);
		String[] fileLineSet = new String[wordCount];
		String[] wordParts = new String[2];
		Random random = new Random();
		Word word;

		for (int j = 0; j < fileLineSet.length; j++) {
			int num = random.nextInt(fileList.size());
			fileLineSet[j] = fileList.remove(num);
		}

		for (int i = 0; i < fileLineSet.length; i++) {
			wordParts = fileLineSet[i].split(",");
			word = new Word(wordParts[0], wordParts[1]);
			words.add(word);
			notFoundWords.add(word);
		}

		return words;
	}

	/**
	 * Creates an instance of the board using the size and theme passed in from the
	 * user.
	 * 
	 * @author Corey Kuehl
	 * @param size
	 *            is the desired size for the board.
	 * @param theme
	 *            is the desired theme for the board.
	 * @return true if the instance of the board was created properly.
	 */
	public boolean createBoard(String size, String theme) {

		if (size.equals("small")) {

			// Get the list of words to be used for the word search.
			words = getRandomWordList(theme + "_small", wordMaxSmall);
			
			// Create a new instance of the board based on the chosen size.
			board = new char[small][small];

			return true;

		} else if (size.equals("medium")) {

			// Get the list of words to be used for the word search.
			words = getRandomWordList(theme + "_medium", wordMaxMedium);
			
			// Create a new instance of the board based on the chosen size.
			board = new char[medium][medium];

			return true;

		} else if (size.equals("large")) {

			// Get the list of words to be used for the word search.
			words = getRandomWordList(theme + "_large", wordMaxLarge);
			
			// Create a new instance of the board based on the chosen size.
			board = new char[large][large];

			return true;

		} else {

			return false;
		}

	}

	/**
	 * Sets random coordinate values for each word of the passed in ArrayList.
	 * Analyzes board array for any open, valid locations that can fit the current
	 * Word being iterated upon.
	 * 
	 * Next, randomly picks from these valid coordinate ranges and assigns the
	 * Word's coordinate list to be somewhere randomly chose within this valid
	 * coordinate range.
	 * 
	 * Note: in rare circumstances, several very large words may not be able to be
	 * added to the board as they may not fit; these words are removed from the list
	 * and not added to the board.
	 * 
	 * @author Colom Boyle
	 * 
	 */
	public void setRandomCoordinates() {
		Random r = new Random();

		// create arraylist to hold filled array locations (row and column represented
		// as one digit)
		ArrayList<Integer> filledElements = new ArrayList<>();

		// loop through word list
		for (int i = 0; i < words.size(); i++) {
			// create arraylist to hold valid coordinate ranges
			ArrayList<CoordinateRange> validCoordinates = new ArrayList<>();

			// get current word
			Word word = words.get(i);

			// search for open index ranges that can fit the current word
			search(filledElements, validCoordinates, word.getWord().length());

			// check that word can be placed at all
			if (validCoordinates.size() > 0) {

				CoordinateRange coordRange = validCoordinates.get(r.nextInt(validCoordinates.size()));

				int offset = r.nextInt(coordRange.getCoordinates().size() - word.getWord().length() + 1);

				// set current word's coordinate values. Add coordinate to filledElements list,
				// as this coordinate will now be used by the word.

				int start = 0;
				int end = word.getWord().length();
				int increment = 1;

				boolean isInverted = r.nextBoolean();

				if (isInverted) {
					start = end - 1;
					end = -1;
					increment = -1;
				}

				for (int j = start; j != end; j += increment) {
					// add word coordinate integer
					int currentCoord = coordRange.getCoordinates().get(j + offset);
					word.getCoordinates().add(currentCoord);
					filledElements.add(currentCoord);
				}
				// remove word that cannot fit into board (has no valid coordinates to be placed
				// on)
			} else {
				// remove current word
				words.remove(word);
			}

		}

	}

	/**
	 * Finds all possible valid coordinate ranges word a word of the passed in size
	 * value. Searches horizontal, vertical, and diagonal ranges.
	 * 
	 * @author Colom Boyle
	 * 
	 * @param filledElements
	 *            Contains elements that are already filled.
	 * @param validCoordinates
	 *            A list of valid coordinate ranges for content to be added to.
	 * @param size
	 *            The size of the word that spaces are being searched for.
	 */
	private void search(ArrayList<Integer> filledElements, ArrayList<CoordinateRange> validCoordinates, int size) {

		CoordinateRange coordRange = new CoordinateRange();

		// horizontal
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				coordRange = coordProcess(coordRange, validCoordinates, filledElements, i, j, size);
			}
			coordRange = finalCoordValidation(coordRange, validCoordinates, size);
		}

		coordRange = new CoordinateRange();

		// vertical
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				coordRange = coordProcess(coordRange, validCoordinates, filledElements, j, i, size);
			}
			coordRange = finalCoordValidation(coordRange, validCoordinates, size);
		}

		coordRange = new CoordinateRange();

		// diagonal up
		int row = 0;
		int col = 0;

		for (int i = 0; i < board.length * 2 - 1; i++) {
			// if out of bounds
			if (i > board[0].length - 1) {
				row++;
				col = board[0].length - 1;
			} else {
				row = 0;
				col = i;
			}

			// down left
			diagonalSearch(row, col, true, false, coordRange, validCoordinates, filledElements, size);
			coordRange = finalCoordValidation(coordRange, validCoordinates, size);
		}

		coordRange = new CoordinateRange();

		// diagonal down
		row = 0;
		col = 0;

		for (int i = 0; i < board.length * 2 - 1; i++) {
			// if out of bounds
			if (i > board[0].length - 1) {
				row++;
				col = board[0].length - 1;
			} else {
				row = 0;
				col = i;
			}

			// down right
			diagonalSearch(row, board[row].length - 1 - col, true, true, coordRange, validCoordinates, filledElements,
					size);
			coordRange = finalCoordValidation(coordRange, validCoordinates, size);
		}
	}

	/**
	 * Searches an array diagonally based on passed in boolean values.
	 * 
	 * @author Colom Boyle
	 * 
	 * @param row
	 *            Start row.
	 * @param col
	 *            Start col.
	 * @param isVerticalDown
	 *            Diagonal is down.
	 * @param isHorizontalRight
	 *            Diagonal is right.
	 * @param coordRange
	 *            A coordinateRange object.
	 * @param validCoordinates
	 *            A list of valid coordinateRange objects.
	 * @param filledElements
	 *            A list of currently filled array elements.
	 * @param size
	 *            The size of the word to search for.
	 */
	private void diagonalSearch(int row, int col, boolean isVerticalDown, boolean isHorizontalRight,
			CoordinateRange coordRange, ArrayList<CoordinateRange> validCoordinates, ArrayList<Integer> filledElements,
			int size) {

		int rowOffset = 1;
		int colOffset = 1;

		// set offset for upward search
		if (!isVerticalDown) {
			rowOffset = -1;
		}

		// set offset for downward search
		if (!isHorizontalRight) {
			colOffset = -1;
		}

		// iterate through diagonal
		while (row >= 0 && col >= 0 && row < board.length && col < board[row].length) {
			coordRange = coordProcess(coordRange, validCoordinates, filledElements, row, col, size);
			row += rowOffset;
			col += colOffset;
		}

	}

	/**
	 * Processes the passed in coordinateRange object to add to the object's list of
	 * valid coordinates.
	 * 
	 * @author Colom Boyle
	 * 
	 * @param coordRange
	 *            A coordinateRange for values to be added to.
	 * @param validCoordinates
	 *            A list of valid coordinate ranges.
	 * @param filledElements
	 *            A list of elements that will be filled on the array.
	 * @param i
	 *            Row.
	 * @param j
	 *            Column.
	 * @param size
	 *            Word size.
	 * @return Returns the passed in CoordinateRange object with added data or
	 *         returns a new CoordinateRange object if coordRange was added to
	 *         validCoordinates.
	 */
	private CoordinateRange coordProcess(CoordinateRange coordRange, ArrayList<CoordinateRange> validCoordinates,
			ArrayList<Integer> filledElements, int i, int j, int size) {

		if (!filledElements.contains(Validator.coordToInt(i, j, board[i].length))) {

			coordRange.getCoordinates().add(Validator.coordToInt(i, j, board[i].length));
		} else {

			if (coordRange.getSize() >= size) {
				validCoordinates.add(coordRange);
			}

			coordRange = new CoordinateRange();
		}

		return coordRange;
	}

	/**
	 * Adds coordRange if it is greater than or equal to the size of the word
	 * (size).
	 * 
	 * @author Colom Boyle
	 * 
	 * @param coordRange
	 *            A CoordinateRange object.
	 * @param validCoordinates
	 *            A list of valid coordinates.
	 * @param size
	 *            A size value representing a word's size.
	 * @return Returns a new CoordinateRange object. Adds coordRange to
	 *         validCoordinates if coordRange is greater than or equal to size.
	 */
	private CoordinateRange finalCoordValidation(CoordinateRange coordRange,
			ArrayList<CoordinateRange> validCoordinates, int size) {

		if (coordRange.getSize() >= size) {
			validCoordinates.add(coordRange);
		}

		coordRange = new CoordinateRange();
		return coordRange;
	}

	/**
	 * 
	 * Sets both pairs of row and column values based on the word's coordinate
	 * ArrayList. (Front and end of word).
	 * 
	 * @author Colom Boyle
	 * 
	 */
	private void assignWordRowsAndCols() {
		for (int i = 0; i < words.size(); i++) {
			Word word = words.get(i);

			// get word's first pair of coordinates
			int[] coordinates1 = Validator.intToCoord(word.getCoordinates().get(0), board[0].length);
			// get word's last pair of coordinates
			int[] coordinates2 = Validator.intToCoord(word.getCoordinates().get(word.getCoordinates().size() - 1),
					board[0].length);

			// set first coordinate pair
			word.setRow1(coordinates1[0]);
			word.setCol1(coordinates1[1]);

			// set last coordinate pair
			word.setRow2(coordinates2[0]);
			word.setCol2(coordinates2[1]);
		}
	}

	/**
	 * Iterates through the word ArrayList and uses the current word's coordinates
	 * to placed the word's characters on the board.
	 * 
	 * @author Colom Boyle
	 */
	private void addWordsToBoard() {
		// loop through word list
		for (int i = 0; i < words.size(); i++) {

			Word currentWord = words.get(i);

			// loop through current word's characters
			for (int j = 0; j < currentWord.getWord().length(); j++) {

				// get coordinates of word's current character
				int[] coordinates = Validator.intToCoord(words.get(i).getCoordinates().get(j), board[0].length);
				int row = coordinates[0];
				int col = coordinates[1];

				// place current character on board
				board[row][col] = currentWord.getWord().charAt(j);

			}
		}
	}

	/**
	 * Fills in a random selection of letters for the gaps in the board that weren't
	 * filled by words.
	 * 
	 * @author Corey Kuehl
	 */
	private void fillBoardGaps() {

		Random random = new Random();

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				int num = (random.nextInt(26) + 97);
				if (board[i][j] == '\0') {
					board[i][j] = (Character.toLowerCase((char) num));
				}
			}
		}
	}

	/**
	 * @author saaniya
	 * @return s <String> String representation of words 
	 * that the user has found in string form
	 */
	public String foundWords() {
		String s = "";

		for (int i = 0; i < foundWords.size(); i++) {
			Word w = foundWords.get(i);
			s += w.getWord() + "\n";
		}

		return s;
	}

	/**
	 * @author saani
	 * @return
	 */
	public String notFoundWords() {
		String s = "";

		for (int i = 0; i < notFoundWords.size(); i++) {
			Word w = notFoundWords.get(i);
			s += w.getWord() + "\n";
		}

		return s;
	}

	/**
	 * @author saaniya
	 * @return
	 */
	public String foundDesc() {
		String s = "";

		for (int i = 0; i < foundWords.size(); i++) {
			Word w = foundWords.get(i);
			s += w.getDescription() + "\n";
		}

		return s;
	}

	/**
	 * @author saaniya
	 * @return <String> of all the words in the puzzle
	 */
	public String allWords() {
		String s = "";

		for (int i = 0; i < words.size(); i++) {
			Word w = words.get(i);
			s += w.getWord() + "\n";
		}

		return s;
	}
	
	/**
	 * @author saaniya
	 * @param w <Word> adds the word to the list of found words 
	 * and removes it from the list of not found words
	 */
	public void setFoundWord(Word w) {
		if (!foundWords.contains(w)) {
			foundWords.add(w);
			notFoundWords.remove(w);
		}
	}


	/**
	 * @author saaniya
	 * @return <String> representation of descriptions that the user has to think about
	 * currently not used, to be impl later with description based words
	 */
	public String allDesc() {
		String s = "";

		for (int i = 0; i < words.size(); i++) {
			Word w = words.get(i);
			s += w.getDescription() + "\n";
		}

		return s;
	}

	// Getters and Setters

	public ArrayList<Word> getWords() {
		return words;
	}

	
	public char[][] getBoard() {
		return board;
	}
	
	public String getSize() {
		// kind of dumb but its 1 am
		return size;
	}
	
	public String getTheme() {
		return theme;
	}

}
