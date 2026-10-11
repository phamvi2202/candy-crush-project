package Main;

import Controller.GameManager;
import Controller.InputHandler;
import View.GameFrame;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Khởi tạo View
            GameFrame frame = new GameFrame();
            
            // Khởi tạo Controller
            GameManager gameManager = new GameManager(frame);
            
            // Khởi tạo Input Handler và gắn nó vào bảng kẹo của View
            InputHandler inputHandler = new InputHandler(gameManager, frame.getBoardPanel());
            frame.getBoardPanel().addMouseListener(inputHandler);
            
            // Hiển thị và chạy game
            frame.setVisible(true);
            gameManager.startGame();
        });
    }
}