package View;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    private JLabel scoreLabel;

    public TopPanel() {
        setBackground(new Color(40, 40, 40));
        scoreLabel = new JLabel("Score: 0   |   Moves: 20   ");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);
        add(scoreLabel);
    }

    public void updateInfo(int score, int movesLeft, int target) {
        scoreLabel.setText("Score: " + score + " / " + target + "   |   Moves: " + movesLeft + "   ");
    }
}