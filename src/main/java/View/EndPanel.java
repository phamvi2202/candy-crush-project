package View;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;

public class EndPanel extends JPanel {
    private JButton btnHome;
    private Image bgImage;
    
    private int failedLevel = 1;
    private int scoreInLevel = 0;

    public EndPanel() {
        setLayout(null); 
        setPreferredSize(new Dimension(520, 700));

        try {
            bgImage = ImageIO.read(new File("src/main/java/assets/end_bg.png"));
        } catch (Exception e) {
            System.out.println("❌ LỖI: Không tìm thấy file end_bg.png");
        }

        btnHome = new JButton();
        
        // --- CHỈNH SỬA TỌA ĐỘ NÚT QUAY LẠI Ở ĐÂY ---
        btnHome.setBounds(110, 560, 300, 80); 

        // BẬT HIỆN VIỀN ĐỂ CĂN CHỈNH NÚT QUAY LẠI (Đổi thành false khi đã khớp)
        btnHome.setOpaque(true); // <--- Đổi thành false khi đã căn xong
        btnHome.setBackground(new Color(255, 0, 0, 100)); 
        
        btnHome.setContentAreaFilled(false);
        btnHome.setBorderPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.setFocusable(false);

        add(btnHome);
    }

    public void setResult(int failedLevel, int scoreInLevel) {
        this.failedLevel = failedLevel;
        this.scoreInLevel = scoreInLevel;
        repaint(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (bgImage != null) {
            g2d.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }

        // --- CHỈNH TỌA ĐỘ CHỮ LEVEL VÀ ĐIỂM SỐ Ở ĐÂY ---
        // Tăng số Y lên thì chữ dịch xuống dưới, giảm thì dịch lên trên
        int levelY = 410; // Tăng lên khoảng 490 để kéo chữ LEVEL xuống
        int scoreY = 470; // Vị trí chiều dọc của chữ ĐIỂM SỐ

        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        String levelStr = "LEVEL " + failedLevel;
        FontMetrics fm = g2d.getFontMetrics();
        int levelX = (getWidth() - fm.stringWidth(levelStr)) / 2; 

        g2d.setColor(new Color(64, 7, 30)); 
        for (int xOffset = -2; xOffset <= 2; xOffset++) {
            for (int yOffset = -2; yOffset <= 2; yOffset++) {
                g2d.drawString(levelStr, levelX + xOffset, levelY + yOffset);
            }
        }
        g2d.setColor(new Color(255, 60, 100)); 
        g2d.drawString(levelStr, levelX, levelY);

        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        String scoreStr = "ĐIỂM SỐ: " + scoreInLevel;
        fm = g2d.getFontMetrics();
        int scoreX = (getWidth() - fm.stringWidth(scoreStr)) / 2;

        g2d.setColor(new Color(80, 40, 10));
        for (int xOffset = -2; xOffset <= 2; xOffset++) {
            for (int yOffset = -2; yOffset <= 2; yOffset++) {
                g2d.drawString(scoreStr, scoreX + xOffset, scoreY + yOffset);
            }
        }
        g2d.setColor(new Color(255, 200, 0)); 
        g2d.drawString(scoreStr, scoreX, scoreY);
    }

    public JButton getBtnHome() { return btnHome; }
}