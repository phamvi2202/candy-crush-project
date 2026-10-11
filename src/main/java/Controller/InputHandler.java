package Controller;

import candycrush.model.Position;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InputHandler extends MouseAdapter {
    private GameManager gameManager; 
    private final int CELL_SIZE = 60; 
    
    private Position firstSelection = null;

    public InputHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int col = e.getX() / CELL_SIZE;
        int row = e.getY() / CELL_SIZE;

        Position clickedPos = new Position(row, col);

        if (firstSelection == null) {
            firstSelection = clickedPos;
            System.out.println("Chọn kẹo tại: " + firstSelection);
        } else {
            System.out.println("Đổi với kẹo tại: " + clickedPos);
            
            // Chuyển việc xử lý sang GameManager
            gameManager.processPlayerMove(firstSelection, clickedPos);
            
            // Reset lựa chọn
            firstSelection = null;
        }
    }
}