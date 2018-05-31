package edu.ben.wordsearch;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * 
 * @author saaniya
 *
 */
@SuppressWarnings("serial")
public class WordSearchGUI extends JFrame {

	private JFrame thisFrame;
	private JButton[][] tiles;
	private JPanel contentPane;
	private int fontSize;
	private int size;
	private Board gameBoard;
	private JPanel foundWords, notFoundWords;

	/**
	 * @param gameBoard<Board> constructor
	 */
	public WordSearchGUI(Board gameBoard) {
		super();
		this.size = gameBoard.getBoard().length;
		this.gameBoard = gameBoard;

		// initialize the contentpane
		initialize(size);

		// add list of words to the right of the word search		
		addWordList(); 
		
		// add the puzzle to the contentpane
		addPuzzlePanel();
		
//		addDescList(); // add list of word definitiosn underneath the puzzle
		addFoundWordsPanel();
		
		// connect AL to update jpanels 
		setPanelsToButtons();
		// pack();

		thisFrame = this;
		addMenu();
		
	}

	/**
	 * @author saani
	 */
	private void setPanelsToButtons() {

		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				
				LetterSelectActionListener aL = (LetterSelectActionListener) 
						tiles[i][j].getActionListeners()[0];
				aL.setFoundsPanel(foundWords);
				aL.setNotFoundsPanel(notFoundWords);
				
			}
		}
		
	}

	/**
	 * actual JFrame that has game
	 * holds all other jpanels
	 * @author saani
	 * @param size <int> of the puzzle
	 */
	private void initialize(int size) {
		// for cross platforming (ugh apple)
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Dimension minSize = new Dimension(1200, 700); // also tried 1150, 610
		setMinimumSize(minSize); 

		// open at max if medium or large puzzle
		// smaller font if larger puzzle
		if (size == 20) {
			setExtendedState(MAXIMIZED_BOTH);
			fontSize = 25;
		} else if (size == 30) {
			setExtendedState(MAXIMIZED_BOTH);
			fontSize = 8;//19;
		} else {
			fontSize = 30;
		}
		
		// centers the JFrame to the middle of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

		contentPane = new JPanel();
		setContentPane(contentPane); // add contentPane to JFrame

		GridBagLayout paneLayout = new GridBagLayout();
		paneLayout.columnWidths = new int[] { 0, 0, 0, 0 }; // 4 cols
		paneLayout.rowHeights = new int[] { 0, 0, 0 }; // 3 rows

		contentPane.setLayout(paneLayout); // 3 rows, 4 cols
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		// insets of the main contentPane

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * @author saaniya
	 * adds jpanel that shows the words that the user found in the puzzle
	 */
	private void addFoundWordsPanel() {
		
		foundWords = new JPanel();
		
		// add uneditable jtext area
		// this will hold the words!
		JTextArea foundWordsText = new JTextArea();
		foundWordsText.setMargin(new Insets(5, 5, 5, 5));
		foundWordsText.setFont(new Font("serif", Font.PLAIN, 20));
		foundWordsText.setText("Words found:\n" + gameBoard.foundWords());
		// list of word objects

		foundWordsText.setEditable(false);
//		wordsList.setLineWrap(true);
		foundWordsText.setWrapStyleWord(true);
		foundWordsText.setOpaque(false);
		
		foundWords.add(foundWordsText);

		GridBagConstraints wordsGBC = new GridBagConstraints();
		wordsGBC.gridx = 2;
		wordsGBC.gridy = 1; // below the 'words that need to be found' word bank
//		wordsListGBC.fill = GridBagConstraints.BOTH;
		wordsGBC.gridheight = 1;
//		wordsListGBC.gridwidth = GridBagConstraints.REMAINDER;
		wordsGBC.anchor = GridBagConstraints.CENTER;
		wordsGBC.weightx = 0.2;
		wordsGBC.weighty = 0.9;

		contentPane.add(foundWords, wordsGBC);
	}

	/**
	 * @author saaniya
	 * adds a jpanel that holds the jbuttons that represents the puzzle 
	 */
	private void addPuzzlePanel() {
		// this holds the word search puzzle!
		JPanel puzzle = new JPanel();

		// simple grid to hold all JButtons
		puzzle.setLayout(new GridLayout(size, size, 0, 0));

		tiles = new JButton[size][size];
		char[][] board = gameBoard.getBoard();
		
		// each tile has the same char
		// as the corresponding array cell from the back end.
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				
				tiles[i][j] = new JButton();
				tiles[i][j].setText(Character.toString(board[i][j]));
				// compare to char from puzzle in the back end
				
				tiles[i][j].setFont(new Font("serif", Font.PLAIN, fontSize));
				tiles[i][j].setContentAreaFilled(false);

				// set action listeners for each button
				ActionListener select = new LetterSelectActionListener(i, j, tiles, gameBoard);
				tiles[i][j].addActionListener(select);
				
				puzzle.add(tiles[i][j]); // add tiles to jpanel
			}
		}

		// puzzle constraints relative to main JFrame
		GridBagConstraints puzzleGBL = new GridBagConstraints();
		puzzleGBL.gridx = 0;
		puzzleGBL.gridy = 0;
		puzzleGBL.gridheight = GridBagConstraints.RELATIVE;
		puzzleGBL.gridwidth = 2; // takes 1/2 of the space
		
		// take extra whitespace for itself
		puzzleGBL.fill = GridBagConstraints.BOTH;
		puzzleGBL.weightx = 0.8;
		puzzleGBL.weighty = 0.8;

		contentPane.add(puzzle, puzzleGBL); // add puzzle with constraints

	}
	
	/**
	 * @author saaniya
	 * adds a jpanel for the word bank
	 */
	private void addWordList() {

		notFoundWords = new JPanel();
		
		// add uneditable jtext area
		// this will hold the words!
		JTextArea wordsList = new JTextArea();
		wordsList.setMargin(new Insets(5, 5, 5, 5));
		wordsList.setFont(new Font("serif", Font.PLAIN, 20));
		wordsList.setText("Words to find:\n" + gameBoard.allWords());
		// from the file/back end
		// list of word objects

		wordsList.setEditable(false);
//		wordsList.setLineWrap(true);
		wordsList.setWrapStyleWord(true);
		wordsList.setOpaque(false);
		
		notFoundWords.add(wordsList);

		GridBagConstraints wordsListGBC = new GridBagConstraints();
		wordsListGBC.gridx = 2;
		wordsListGBC.gridy = 0;
//		wordsListGBC.fill = GridBagConstraints.BOTH;
		wordsListGBC.gridheight = 1;
//		wordsListGBC.gridwidth = GridBagConstraints.REMAINDER;
		wordsListGBC.anchor = GridBagConstraints.NORTH;
		wordsListGBC.weightx = 0.1;
		wordsListGBC.weighty = 0.2;

		contentPane.add(notFoundWords, wordsListGBC);
		
		// add a panel for found words 

	}
	
	/**
	 * @author saaniya
	 */
	private void addDescList() {
		
		JPanel panel = new JPanel();
		JTextArea descList = new JTextArea();
		descList.setMargin(new Insets(5, 5, 5, 5));
		descList.setFont(new Font("serif", Font.PLAIN, 20));
		descList.setText(gameBoard.allDesc());
		
		// from the file/back end
		// list of word objects

		descList.setEditable(false);
		descList.setLineWrap(true);
//		descList.setWrapStyleWord(true);
		descList.setOpaque(false);
		
		GridBagConstraints descListGBC = new GridBagConstraints();
		descListGBC.gridx = 2;
		descListGBC.gridy = 0;
//		wordsListGBC.fill = GridBagConstraints.BOTH;
		descListGBC.gridheight = 1;
//		wordsListGBC.gridwidth = GridBagConstraints.REMAINDER;
		descListGBC.anchor = GridBagConstraints.NORTHWEST;
		descListGBC.weightx = 0.1;
		descListGBC.weighty = 0.2;

		JScrollPane scroller = new JScrollPane();
		scroller.add(descList);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
		panel.add(scroller);
		
		contentPane.add(scroller, descListGBC);
		
	}
	
	/**
	 * @author saani
	 */
	public void addMenu() {
		// jmenubar -> jmenu -> jmenuitme
		JMenuBar menuBar = new JMenuBar();
		
		JMenu info = new JMenu("Help");
		menuBar.add(info);
		
		JMenuItem instr = new JMenuItem("Instructions");
		instr.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new JFrame(); 
				frame.setMinimumSize(new Dimension(500,270));
				frame.setLocation(750, 100); //top right ish
				JTextArea instructions = new JTextArea();
				
				instructions.setMargin(new Insets(5, 5, 5, 5));
				instructions.setFont(new Font("serif", Font.PLAIN, 20));
				instructions.setText("How to play the game\n"
						+ "Look for the words from the word bank\n"
						+ "in the puzzle. When you think you've \n"
						+ "found a word, click the first and last letter\n"
						+ "of the word (order doesn't matter). If it's a \n"
						+ "correct solution, the tiles will be colored green.");
				// list of word objects

				instructions.setEditable(false);
				instructions.setLineWrap(true);
				instructions.setWrapStyleWord(true);
				instructions.setOpaque(false);
				
				frame.getContentPane().add(instructions);
				frame.setVisible(true);
				
			}
			
		});
		
		
		JMenuItem restart = new JMenuItem("Restart Game");
		restart.addActionListener(new ActionListener() {
			

			@Override
			public void actionPerformed(ActionEvent e) {
				// close current game
				
				
				restart();
			}
			
		});
		
		info.add(instr);
		info.add(restart);
		
		this.setJMenuBar(menuBar);
	}


	/**
	 * @author saaniya
	 * restart button
	 */
	private void restart() {
		thisFrame.setVisible(false);
		
		String sizeStr = "";
		
		if (size == 10) {
			sizeStr = "small";
		} else if (size == 20) {
			sizeStr = "medium";
		} else {
			sizeStr = "large";
		}
		
		Board b = new Board(sizeStr, gameBoard.getTheme());
		this.gameBoard = b;
//		WordSearchGUI newFrame = new WordSearchGUI(b);

		thisFrame = new WordSearchGUI(b);
		thisFrame.setVisible(true);
		
	}
	
	/**
	 * @author saaniya
	 * @return theme<String> user's choice for theme of the puzzle,
	 * affects what words are going to be in the puzzle
	 */
	public static String chooseTheme() {

		// me being extra. pls dont erase yet
		// leave as reference for other jframes

		// // create jframe,
		// // add to it a jpanel
		// // set layout and layout manager for jpanel
		// // add combobox
		// // add ok button
		// // set AL to ok button based on combo box selection
		// try {
		// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// JFrame themeSel = new JFrame();
		// themeSel.setTitle("select theme");
		// JPanel contentPane = new JPanel();
		// themeSel.setContentPane(contentPane);
		//
		//
		// Dimension minSize = new Dimension(500, 150); // default size of the JFrae
		// themeSel.setMinimumSize(minSize);
		//
		// Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// themeSel.setLocation(dim.width/2-themeSel.getSize().width/2,
		// dim.height/2-themeSel.getSize().height/2);
		// // center the JFrame in the middle of the screen
		//
		//
		// // 3 col, 1 row in this jframe.
		// // 2 col for dropdown menu 1 col for ok button
		//
		// GridBagLayout themeSel_layout = new GridBagLayout();
		// themeSel_layout.columnWidths = new int[] {0, 0};
		// themeSel.setLayout(themeSel_layout);
		// // set gridlayout for JFrame
		//
		// // add jtext area that describes instructions? do it here.
		//
		// JComboBox<String> comboBox = new JComboBox<String>();
		//
		// // could these be obtained from the backend?
		// comboBox.addItem("theme 1");
		// comboBox.addItem("theme 2");
		//
		// GridBagConstraints comboBoxConstr = new GridBagConstraints();
		// comboBoxConstr.gridx = 0;
		// comboBoxConstr.gridwidth = 2; //2 cells wide
		// comboBoxConstr.fill = GridBagConstraints.HORIZONTAL;
		// // set constraints of combobox
		//
		// contentPane.add(comboBox, comboBoxConstr);
		//
		// JButton ok = new JButton("ok"); // add AL
		//
		// // action listener should close the window,
		// // return the selected value to the method that called it
		//
		//// ok.setContentAreaFilled(false);
		//// ok.setSize(contentPane.getWidth()/3, contentPane.getHeight());
		//
		// contentPane.add(ok); // constraints necessary?
		//
		// themeSel.setVisible(true);
		//
		// // add action listeneres
		//

		// below is the simple way to do it

		String[] themes = { "Software Engineering", "Advanced Web" };
		String s = (String) JOptionPane.showInputDialog(new JFrame(), "Choose", "Theme/Size",
				JOptionPane.INFORMATION_MESSAGE, null, themes, themes[0]);

		System.out.println(s); // this can be null if the user clicks cancel.
		return s;
	}

	/**
	 * @author saaniya
	 * @return size <String> the users choice of puzzle size (small, medium, or large)
	 */
	public static String chooseSize() {

		// drop down menu instead of radio buttons for simplicity
		String[] sizes = { "small", "medium", "large" };
		String s = (String) JOptionPane.showInputDialog(new JFrame(), "Choose the size of the puzzle", 
				"Theme/Size", JOptionPane.INFORMATION_MESSAGE, null, sizes, sizes[0]);

		System.out.println(s); // this can be null if the user clicks cancel.

		return s;

		// will experiemnt with this later for jradio buttons

		// JPanel panel = new JPanel();
		// panel.add(new JRadioButton("radio");
		// JOptionPane.showOptionDialog(null, panel,
		// "Radio Test", JOptionPane.YES_NO_CANCEL_OPTION,
		// JOptionPane.QUESTION_MESSAGE, null, null, null);

	}

}
