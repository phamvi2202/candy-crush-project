package View;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameFrame extends JFrame {
    private TopPanel topPanel;
    private BoardPanel boardPanel;

    public GameFrame() {
        setTitle("Candy Crush - Chuẩn MVC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Khởi tạo các thành phần con
        BufferedImage[] candyImages = AssetManager.loadCandyImages();
        topPanel = new TopPanel();
        boardPanel = new BoardPanel(candyImages);

        // Lắp ráp vào Frame
        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public TopPanel getTopPanel() { return topPanel; }
    public BoardPanel getBoardPanel() { return boardPanel; }

    public void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}