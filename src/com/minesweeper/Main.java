package com.minesweeper;

import javax.swing.*;

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
