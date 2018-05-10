package com.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MinesweeperGUI extends JFrame{
    private JPanel mainPanel;
    private JPanel mainMenu;
    private JButton playButton;
    private JButton settingsButton;
    private JButton exitButton;
    private JPanel scoreMenu;
    private JPanel settingsMenu;
    private JPanel gameScreen;

    private JRadioButton beginnerRadioButton;
    private JButton beginButton;
    private JPanel gameSelectMenu;
    private JRadioButton intermediateRadioButton;
    private JRadioButton expertRadioButton;
    private JButton backButton;
    private JCheckBox visibleMinesCheckBox;
    private JButton returnToMenuButton;

    private static String MAIN_MENU = "Main menu";
    private static String SETTINGS_MENU = "Settings menu";
    private static String GAME_SELECT = "Menu where player decides what size of game to play";
    private static String GAME_SCREEN = "Minesweeper game screen";


    public Cell[][] grid;
    private boolean visibleMines = false;
    private boolean gameOver = false;

    public MinesweeperGUI(){
        setContentPane(mainPanel);
        pack();
        centerGUI();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainPanel.add(mainMenu, MAIN_MENU);
        mainPanel.add(settingsMenu, SETTINGS_MENU);
        mainPanel.add(gameSelectMenu, GAME_SELECT);
        mainPanel.add(gameScreen, GAME_SCREEN);

        this.setResizable(false);

        // Play button pressed, go to game select
        playButton.addActionListener(e -> {
            // Play button pushed, go to game select screen
            CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
            cardLayout.show(mainPanel, GAME_SELECT);
        });

        // Settings button pressed, go to settings menu
        settingsButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
            cardLayout.show(mainPanel, SETTINGS_MENU);
        });

        // Exit button pressed, close window
        exitButton.addActionListener(e -> dispose());

        // Begin game pushed, validate a game size has been selected and start it.
        beginButton.addActionListener(e -> {
            JRadioButton gameMode = getGameMode();
            // Input Validation -- Ensure user selects a game mode before trying to proceed to the game
            if (gameMode.getText().equals("")){
                JOptionPane.showMessageDialog(Main.gui, "Please select a game mode!", "Minesweeper", JOptionPane.WARNING_MESSAGE);
            }
            // If user has selected a game mode, modify the window size to correctly fit the number of cells
            else{
                CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
                GridLayout gridLayout;

                // Adjust screen size to fit number of cells and prepare game board
                switch (gameMode.getText()){
                    case "Beginner (8 x 8, 10 mines)":
                        mainPanel.setPreferredSize(new Dimension(325, 325));
                        gridLayout = new GridLayout(8, 8, 0, 0);
                        gameScreen.setLayout(gridLayout);
                        prepareGameBoard(GameSize.BEGINNER);
                        break;

                    case "Intermediate (16 x 16, 40 mines)":
                        mainPanel.setPreferredSize(new Dimension(650, 650));
                        gridLayout = new GridLayout(16, 16, 0, 0);
                        gameScreen.setLayout(gridLayout);
                        prepareGameBoard(GameSize.INTERMEDIATE);
                        break;

                    case "Expert (24 x 24, 99 mines)":
                        mainPanel.setPreferredSize(new Dimension(975, 975));
                        gridLayout = new GridLayout(24, 24, 0, 0);
                        gameScreen.setLayout(gridLayout);
                        prepareGameBoard(GameSize.EXPERT);
                        break;
                }
                cardLayout.show(mainPanel, GAME_SCREEN);
                pack();
                centerGUI();
            }
        });

        // Back button pressed, return to main menu
        backButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
            cardLayout.show(mainPanel, MAIN_MENU);
        });

        // Return to menu button pressed, return to menu
        returnToMenuButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
            cardLayout.show(mainPanel, MAIN_MENU);

            if (visibleMinesCheckBox.isSelected()){
                visibleMines = true;
            }
        });

    }

    private void prepareGameBoard(GameSize gameSize) {

        // Initialize the game board with the correct number of cells
        switch (gameSize) {

            case BEGINNER:
                grid = new Cell[8][8];
                break;

            case INTERMEDIATE:
                grid = new Cell[16][16];
                break;

            case EXPERT:
                grid = new Cell[24][24];
                break;
        }
        initializeCells();
        placeMines(gameSize);
        countAdjacentMines();

        // Show layout of mines in terminal (debugging purposes only)
        if (visibleMines) {
            System.out.println("=====DEBUG=====");

            for (int i = 0; i < grid.length; i++){
                for (int j = 0; j < grid[i].length; j++){
                    System.out.print(grid[i][j]);
                }
                System.out.println();
            }
            System.out.println("===============");
        }
    }

    private void initializeCells() {
        // Create a new Cell object for each space in the grid, and add them to the screen
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Cell cell = new Cell();
                grid[i][j] = cell;
                gameScreen.add(cell, getIndex(cell));
            }
        }
    }

    private void placeMines(GameSize gameSize) {
        // Generates random locations within the game board to place mines, so long as there are still mines to be placed
        Random random = new Random();
        int mineCount;

        // Determine mine count
        switch (gameSize){
            case BEGINNER:
                mineCount = 5;
                break;
            case INTERMEDIATE:
                mineCount = 40;
                break;
            case EXPERT:
                mineCount = 99;
                break;
            default:
                mineCount = 0;
        }

        while (mineCount != 0){
            // Generate a random row, col pair
            int row = random.nextInt(grid.length);
            int col = random.nextInt(grid.length);

            // If row, col pair is not already a mine, set it.
            if (!(grid[col][row].isBomb())){
                grid[col][row].setBomb();
                mineCount--;
            }
        }
    }

    private Index getIndex(Cell cell){
        int index = cell.getIndex(); // get Cell class's index (ex. 12)
        int row = 0;

        // Correctly converts single integer index value (ex. 12) to row, col index value (12 -> 1, 3)
        switch (index) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                return new Index(row, index);
            default:
                // Reduce index by row length, add 1 to row count.
                // 12 - 8 = 4 --> row 1, column 3 (0, 1, 2, 3 = 4 spaces)
                while (index >= grid[0].length) {
                    index -= grid[0].length;
                    row += 1;
                }
        }
        return new Index(row, index);
    }

    // Recursive clear cell function
    public void clear(Cell cell) {
        // Store information about current Cell object
        Index index = getIndex(cell);
        int row = index.getRow();
        int col = index.getCol();
        int remainingCells = 0; // number of non-mine Cell objects still uncleared on the board

        // Base case -- Cell has a neighboring bomb (ie. this Cell has a count of 1 - 8)
        if (neighboringBomb(index)){
            cell.clear();
            cell.showCount();
        }

        // Check that the current Cell object is in bounds, is not a bomb, and has not already been cleared.
        // Then, clear the 8 surrounding cells.
        else {
            if (inBounds(row, col)) {
                if (!cell.isBomb()) {
                    if (!cell.isCleared()) {
                        cell.clear();
                        cell.showCount();

                        if (inBounds(row, col + 1)) {
                            clear(grid[row][col + 1]);
                        }
                        if (inBounds(row + 1, col + 1)) {
                            clear(grid[row + 1][col + 1]);
                        }
                        if (inBounds(row + 1, col)) {
                            clear(grid[row + 1][col]);
                        }
                        if (inBounds(row + 1, col - 1)) {
                            clear(grid[row + 1][col - 1]);
                        }
                        if (inBounds(row, col - 1)) {
                            clear(grid[row][col - 1]);
                        }
                        if (inBounds(row - 1, col - 1)) {
                            clear(grid[row - 1][col - 1]);
                        }
                        if (inBounds(row - 1, col)) {
                            clear(grid[row - 1][col]);
                        }
                        if (inBounds(row - 1, col + 1)) {
                            clear(grid[row - 1][col + 1]);
                        }
                    }
                }
            }
        }

        // Once 8 surrounding cells have been cleared, check to see if any non-mine Cell objects remain uncleared
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[0].length; j++){
                if (!grid[i][j].isCleared() && !grid[i][j].isBomb()) {
                    remainingCells++;
                }
            }
        }

        // If all of the cells that are not mines have been cleared, the player wins.
        if (remainingCells == 0) {
            if (!gameOver) {
                gameOver = true;
                gameOver(true);
            }
        }
    }


    // Returns true if cell at index has any neighboring (adjacent) bombs
    private boolean neighboringBomb(Index index){
        int col = index.getCol();
        int row = index.getRow();

        // Check all 8 spaces around the cell
        if (inBounds(row+1, col)) {
            if (grid[row + 1][col].isBomb()) {
                return true;
            }
        }
        if (inBounds(row-1, col)) {
            if (grid[row - 1][col].isBomb()) {
                return true;
            }
        }
        if (inBounds(row, col-1)) {
            if (grid[row][col - 1].isBomb()) {
                return true;
            }
        }
        if (inBounds(row+1, col-1)) {
            if (grid[row + 1][col - 1].isBomb()) {
                return true;
            }
        }
        if (inBounds(row-1, col-1)) {
            if (grid[row - 1][col - 1].isBomb()) {
                return true;
            }
        }
        if (inBounds(row, col+1)) {
            if (grid[row][col + 1].isBomb()) {
                return true;
            }
        }
        if (inBounds(row+1, col+1)) {
            if (grid[row + 1][col + 1].isBomb()) {
                return true;
            }
        }
        if (inBounds(row-1, col+1)) {
            if (grid[row - 1][col + 1].isBomb()) {
                return true;
            }
        }
        return false;
    }

    private boolean inBounds(int row, int col){
        return ((0 <= row) && (row <= grid.length-1)) && ((0 <= col) && (col <= grid[0].length-1));
    }

    private void countAdjacentMines(){
        for (int row = 0; row < grid.length; row++){
            for (int col = 0; col < grid[0].length; col++){
                Index index = getIndex(grid[row][col]);
                int bombCount = countBombs(index);
                grid[row][col].setCount(bombCount);
            }
        }
    }

    // Counts the number of bombs adjacent to a given cell
    private int countBombs(Index index){
        int col = index.getCol();
        int row = index.getRow();
        int bombCount = 0;

        if (inBounds(row+1, col)) {
            if (grid[row + 1][col].isBomb()) {
                bombCount++;
            }
        }
        if (inBounds(row-1, col)) {
            if (grid[row - 1][col].isBomb()) {
                bombCount++;
            }
        }
        if (inBounds(row, col-1)) {
            if (grid[row][col - 1].isBomb()) {
                bombCount++;
            }
        }
        if (inBounds(row+1, col-1)) {
            if (grid[row + 1][col - 1].isBomb()) {
                bombCount++;
            }
        }
        if (inBounds(row-1, col-1)) {
            if (grid[row - 1][col - 1].isBomb()) {
                bombCount++;
            }
        }
        if (inBounds(row, col+1)) {
            if (grid[row][col + 1].isBomb()) {
                bombCount++;
            }
        }
        if (inBounds(row+1, col+1)) {
            if (grid[row + 1][col + 1].isBomb()) {
                bombCount++;
            }
        }
        if (inBounds(row-1, col+1)) {
            if (grid[row - 1][col + 1].isBomb()) {
                bombCount++;
            }
        }
        return bombCount;
    }

    // Used to determine which size of game the user wants to play
    private JRadioButton getGameMode() {
        if (beginnerRadioButton.isSelected()){
            return beginnerRadioButton;
        }
        else if (intermediateRadioButton.isSelected()){
            return intermediateRadioButton;
        }
        else if (expertRadioButton.isSelected()){
            return intermediateRadioButton;
        }
        else{
            return new JRadioButton(); // new radio button with a .getText() value of "", used to determine if the user selected a game mode or not
        }
    }

    // Centers the GUI window in the middle of the screen
    private void centerGUI() {
        this.setLocationRelativeTo(null);
    }

    public void gameOver(boolean win) {
        String message = win ? "Game Over! You win!" : "Game Over! You lose!";

        // Iterate through remaining cells and show where all the bombs were
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                Cell cell = grid[row][col];
                if (cell.isBomb()){
                    cell.setBackground(Color.red);
                    cell.setOpaque(true);
                }
                else{
                    // If the user lost, clear every cell
                    if (!win) {
                        cell.clear();
                    }
                }

            }
        }
        // Show win/loss screen
        int input = JOptionPane.showOptionDialog(null, message, "Minesweeper", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        if (input == JOptionPane.OK_OPTION || input == JOptionPane.OK_CANCEL_OPTION){
            dispose();
        }
    }
}
