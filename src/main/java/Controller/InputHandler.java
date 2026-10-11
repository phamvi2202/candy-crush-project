package Controller;

import candycrush.model.Position;
import View.BoardPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InputHandler extends MouseAdapter {
    private GameManager gameManager; 
    private BoardPanel boardPanel;
    private Position firstSelection = null;

    public InputHandler(GameManager gameManager, BoardPanel boardPanel) {
        this.gameManager = gameManager;
        this.boardPanel = boardPanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Đang chạy hiệu ứng thì khóa click
        if (boardPanel.isBusy()) return;

        // Nhờ View dịch tọa độ pixel sang tọa độ mảng
        Position clickedPos = boardPanel.getGridPosition(e.getX(), e.getY());
        
        // Click ra ngoài bảng lưới
        if (clickedPos == null) return;

        if (firstSelection == null) {
            firstSelection = clickedPos;
            boardPanel.setSelection(clickedPos.getRow(), clickedPos.getCol()); // Hiện viền chọn
        } else {
            boardPanel.setSelection(-1, -1); // Xóa viền
            
            // Nếu 2 ô kề nhau thì gọi GameManager đổi chỗ
            if (firstSelection.isAdjacentTo(clickedPos)) {
                gameManager.processPlayerMove(firstSelection, clickedPos);
            }
            
            firstSelection = null;
        }
    }
}