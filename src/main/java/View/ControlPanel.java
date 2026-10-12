package View;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private JButton btnEnd, btnReset;
    
    public ControlPanel() {
        setLayout(null); // Dùng Absolute Layout để đặt 2 nút
        setOpaque(false); // Làm trong suốt Panel chứa
        
        btnEnd = new JButton(); 
        btnReset = new JButton();
        
        // CĂN CHỈNH VỊ TRÍ 2 NÚT BÊN TRONG PANEL
        btnEnd.setBounds(0, 0, 190, 60); 
        btnReset.setBounds(210, 0, 190, 60); 
        
        // BẬT HIỆN ĐỎ ĐỂ CĂN CHỈNH (Đổi true thành false khi đã khớp)
        btnEnd.setOpaque(true); // <--- ĐỔI FALSE
        btnEnd.setBackground(new Color(255, 0, 0, 100));
        btnReset.setOpaque(true); // <--- ĐỔI FALSE
        btnReset.setBackground(new Color(255, 0, 0, 100));
        
        btnEnd.setContentAreaFilled(false); 
        btnEnd.setBorderPainted(false);
        btnEnd.setFocusable(false);
        btnEnd.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnReset.setContentAreaFilled(false); 
        btnReset.setBorderPainted(false);
        btnReset.setFocusable(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        add(btnEnd); 
        add(btnReset);
    }
    
    public JButton getBtnEnd() { return btnEnd; }
    public JButton getBtnReset() { return btnReset; }
}