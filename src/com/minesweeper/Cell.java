package com.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
