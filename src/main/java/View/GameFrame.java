package View;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;

public class GameFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel, gameContainer;
    private StartPanel startPanel;
    private EndPanel endPanel;
    private TopPanel topPanel;
    private BoardPanel boardPanel;
    private ControlPanel controlPanel;

    public GameFrame() {
        setTitle("Candy Crush");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        startPanel = new StartPanel();
        endPanel = new EndPanel();
        
        // 1. Tạo màn hình Game vẽ nền game_bg.png
        gameContainer = new JPanel(null) { 
            Image bg;
            {
                try { bg = ImageIO.read(new File("src/main/java/assets/game_bg.png")); } 
                catch (Exception e) { System.out.println("❌ LỖI: Không tìm thấy game_bg.png"); }
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        gameContainer.setPreferredSize(new Dimension(540, 780)); 

        // 2. Khởi tạo và căn chỉnh các Panel đè lên ảnh
        topPanel = new TopPanel();
        topPanel.setBounds(40, 75, 460, 50); 

        boardPanel = new BoardPanel(AssetManager.loadCandyImages());
        // ĐÃ SỬA: Đẩy X sang phải (80) và đẩy Y xuống (174) để kẹo khớp vào các ô trống
       boardPanel.setBounds(86, 195, 370, 460);

        controlPanel = new ControlPanel();
        controlPanel.setBounds(70, 680, 400, 70); 

        // Thêm vào gameContainer
        gameContainer.add(topPanel);
        gameContainer.add(boardPanel);
        gameContainer.add(controlPanel);

        // Thêm vào CardLayout
        mainPanel.add(startPanel, "START");
        mainPanel.add(gameContainer, "GAME");
        mainPanel.add(endPanel, "END");

        add(mainPanel); 
        pack(); 
        setLocationRelativeTo(null); 
        setResizable(false);
        cardLayout.show(mainPanel, "START"); 
    }

    public void showStartScreen() { cardLayout.show(mainPanel, "START"); setTitle("Candy Crush"); }
    public void showGameScreen() { cardLayout.show(mainPanel, "GAME"); }
    public void showEndScreen(int failedLevel, int scoreInLevel) { 
        endPanel.setResult(failedLevel, scoreInLevel);
        cardLayout.show(mainPanel, "END"); 
    }

    public StartPanel getStartPanel() { return startPanel; }
    public EndPanel getEndPanel() { return endPanel; }
    public TopPanel getTopPanel() { return topPanel; }
    public BoardPanel getBoardPanel() { return boardPanel; }
    public ControlPanel getControlPanel() { return controlPanel; }
}