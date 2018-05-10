package com.minesweeper;

/* === Index ===
 * The purpose of this class is to provide a
 * row, col representation of a single-digit index
 * value. When a Cell object is created, it is assigned
 * a single-digit index value, which is essentially equal
 * to the number of cells that have been created.
 *
 * In order to reference Cell objects by their index,
 * it is necessary to convert the single-digit index value
 * to a row, col index value so that you can do things like:
 *
 *      if (grid[row][col].isBomb()){
 *          System.out.println("This cell is a bomb!");
 *      }
 *
 * Written by Alex Mastin
 */

public class Index {
    private int row;
    private int col;

    Index(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
