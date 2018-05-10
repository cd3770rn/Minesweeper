package com.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* === Cell ===
 * This class is where all the magic happens.
 * Cell objects extend JButtons, so that they are easy
 * to click on. This class contains a number of characteristics
 * that you would expect a cell in Minesweeper to have.
 *
 * The cellCount variable keeps track of how many cells have
 * been created, and the index variable exists as a non-static
 * reference to cellCount that can be used to determine the
 * equivalent row, col index value of a Cell object.
 *
 * A cell can be a bomb, and a cell can be cleared. By default,
 * cells are not cleared. When the game is run, Cell objects
 * are randomly selected to be bombs.
 *
 * Later on, the cell is assigned a bombCount if it has any
 * adjacent mines.
 *
 * Written by Alex Mastin
 */

public class Cell extends JButton{
    private boolean isBomb;
    private boolean isCleared;
    private static int cellCount;
    private int index = cellCount;
    private int bombCount;


    Cell(){
        // Set values for this Cell (JButton)
        this.setPreferredSize(new Dimension(25, 25));
        this.setMargin(new Insets(0, 0, 0, 0));
        this.setText("");
        this.setAlignmentY(0.0f);
        cellCount++;

        this.addActionListener(e -> {
            setEnabled(false);

            // If this cell is a bomb, mark it, and end the game.
            if (isBomb()){
                setBackground(Color.red);
                setOpaque(true);
                Main.gui.gameOver(false);
            }

            // Otherwise clear it
            else{
                Main.gui.clear((Cell) e.getSource());
            }
        });
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb() {
        isBomb = true;
    }

    public boolean isCleared() {
        return isCleared;
    }

    // Makes this cell appear as cleared
    public void clear() {
        if (!isCleared) {
            isCleared = true;
            setOpaque(false);
            setEnabled(false);
        }
    }

    // Returns true if this cell has adjacent mines
    public boolean hasCount(){
        return bombCount > 0;
    }

    // Used to set a cell's number of adjacent mines
    public void setCount(int count){
        this.bombCount = count;
    }

    // Sets the text of this cell to bombCount
    public void showCount(){
        if (bombCount != 0) {
            this.setText(String.valueOf(bombCount));
        }
    }

    // Get this cell's single-digit index value
    public int getIndex(){
        return index;
    }

    // Used when visibleMines is true
    @Override
    public String toString() {
        if (this.isBomb) {
            return "[*]";
        }
        else {
            if (bombCount == 0){
                return "[ ]";
            }
            else {
                return "[" + bombCount + "]";
            }
        }
    }
}
