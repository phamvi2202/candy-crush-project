/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
/**
 *
 * @author ADMIN
 */
public class InputHandler extends MouseAdapter {
    private GameManager gameManager;
    private final int CELL_SIZE = 60; 
    
    private int firstRow = -1;
    private int firstCol = -1;

    public InputHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int col = e.getX() / CELL_SIZE;
        int row = e.getY() / CELL_SIZE;

        if (firstRow == -1 && firstCol == -1) {
            firstRow = row;
            firstCol = col;
            System.out.println("Chon keo tai: (" + firstRow + ", " + firstCol + ")");
        } else {
            System.out.println("Doi voi keo tai: (" + row + ", " + col + ")");
            gameManager.processPlayerMove(firstRow, firstCol, row, col);
            
            firstRow = -1;
            firstCol = -1;
        }
    }
    
}
