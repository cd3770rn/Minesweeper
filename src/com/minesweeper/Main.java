package com.minesweeper;

import javax.swing.*;

/* === Main ===
 * This is the Main class, which runs a new instance
 * of Minesweeper GUI, and also sets the look and feel
 * for the game.
 *
 * Written by Alex Mastin
 */

public class Main {
    public static MinesweeperGUI gui;
    public static void main(String[] args) {
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        gui = new MinesweeperGUI();

    }
}
