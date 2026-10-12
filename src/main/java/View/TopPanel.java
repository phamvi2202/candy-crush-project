package View;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    private JLabel infoLabel;

    public TopPanel() {
        // Dùng BorderLayout để ép nội dung tự động ra giữa
        setLayout(new BorderLayout()); 
        setOpaque(false); 

        // SwingConstants.CENTER giúp text luôn nằm chính giữa bảng
        infoLabel = new JLabel("", SwingConstants.CENTER); 
        
        // Thu nhỏ size chữ từ 22 xuống 18
        infoLabel.setFont(new Font("Arial", Font.BOLD, 18)); 
        infoLabel.setForeground(new Color(255, 230, 150)); 
        
        add(infoLabel, BorderLayout.CENTER);
    }

    public void updateInfo(int level, int score, int movesLeft, int target) {
        infoLabel.setText("Level: " + level + "  |  Score: " + score + " / " + target + "  |  Moves: " + movesLeft);
    }
}