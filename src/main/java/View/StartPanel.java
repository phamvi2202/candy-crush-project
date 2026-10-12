package View;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;

public class StartPanel extends JPanel {
    private JButton btnStart;
    private Image bgImage;

    public StartPanel() {
        setLayout(null); 
        setPreferredSize(new Dimension(520, 700)); // Kích thước gần bằng bảng kẹo

        try {
            bgImage = ImageIO.read(new File("src/main/java/assets/start_bg.png"));
        } catch (Exception e) {
            System.out.println("❌ LỖI: Không tìm thấy file start_bg.png");
        }

        btnStart = new JButton();
        
        // --- CHỈNH SỬA TỌA ĐỘ NÚT START Ở ĐÂY ---
        // (Tọa độ X, Tọa độ Y, Chiều Rộng, Chiều Cao)
        btnStart.setBounds(140, 340, 240, 80); 

        // BẬT HIỆN VIỀN ĐỂ CĂN CHỈNH: Đang để màu đỏ mờ, khi nào khớp với ảnh rồi thì đổi "true" thành "false"
        btnStart.setOpaque(true); // <--- Đổi thành false khi đã căn xong
        btnStart.setBackground(new Color(255, 0, 0, 100)); 
        
        btnStart.setContentAreaFilled(false);
        btnStart.setBorderPainted(false);
        btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnStart.setFocusable(false);

        add(btnStart);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public JButton getBtnStart() { return btnStart; }
}