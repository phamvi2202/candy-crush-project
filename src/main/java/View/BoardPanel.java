package View;

import candycrush.model.Position;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class BoardPanel extends JPanel {
    // TILE_SIZE và PADDING dùng để căn chỉnh kẹo cho khớp với ảnh nền
    private final int ROWS = 10, COLS = 8;
    private int TILE_SIZE = 45; 
    private int PADDING = 0;   
    private int[][] board;
    private Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.MAGENTA};
    private BufferedImage[] candyImages;

    private int selectedRow = -1, selectedCol = -1;

    // --- Biến Animation ---
    private boolean isAnimating = false, isExploding = false, isDropping = false;
    private Timer timer, explosionTimer, dropTimer;
    private int animProgress = 0, r1, c1, r2, c2, dx, dy, currentExplosionSize;
    private List<Point> explodingCandies = new ArrayList<>();
    private List<FallingCandy> fallingCandies = new ArrayList<>();

    class FallingCandy {
        int type, col, endRow;
        float currentY, targetY;
        public FallingCandy(int type, int col, int startRow, int endRow) {
            this.type = type; this.col = col; this.endRow = endRow;
            this.currentY = PADDING + (startRow * TILE_SIZE);
            this.targetY = PADDING + (endRow * TILE_SIZE);
        }
    }

    public BoardPanel(BufferedImage[] images) {
        this.candyImages = images;
        // Đã xóa setPreferredSize và setBackground
        setOpaque(false); // QUAN TRỌNG: Làm trong suốt nền để thấy ảnh game_bg.png
        board = new int[ROWS][COLS];
    }

    public void updateBoard(int[][] newBoard) { this.board = newBoard; repaint(); }

    // ====== HÀM HỖ TRỢ CHO INPUT_HANDLER ======
    public Position getGridPosition(int x, int y) {
        int c = (x - PADDING) / TILE_SIZE;
        int r = (y - PADDING) / TILE_SIZE;
        if (r >= 0 && r < ROWS && c >= 0 && c < COLS) return new Position(r, c);
        return null;
    }
    
    public boolean isBusy() { return isAnimating || isExploding || isDropping; }
    
    public void setSelection(int r, int c) {
        this.selectedRow = r; this.selectedCol = c;
        repaint();
    }
    // ===========================================

    public void startSwapAnimation(int row1, int col1, int row2, int col2, Runnable onComplete) {
        isAnimating = true; r1 = row1; c1 = col1; r2 = row2; c2 = col2; dx = c2 - c1; dy = r2 - r1; animProgress = 0;
        timer = new Timer(10, e -> {
            animProgress += 5;
            if (animProgress >= TILE_SIZE) {
                timer.stop(); isAnimating = false;
                int temp = board[r1][c1]; board[r1][c1] = board[r2][c2]; board[r2][c2] = temp;
                if (onComplete != null) onComplete.run();
            }
            repaint();
        });
        timer.start();
    }

    public void startExplosionAnimation(List<Point> candies, Runnable onComplete) {
        if (candies == null || candies.isEmpty()) { if (onComplete != null) onComplete.run(); return; }
        this.explodingCandies = candies; this.isExploding = true; this.currentExplosionSize = TILE_SIZE - 10;
        explosionTimer = new Timer(15, e -> {
            currentExplosionSize -= 5;
            if (currentExplosionSize <= 0) {
                explosionTimer.stop(); isExploding = false;
                for (Point p : explodingCandies) board[p.x][p.y] = -1;
                explodingCandies.clear();
                if (onComplete != null) onComplete.run();
            }
            repaint();
        });
        explosionTimer.start();
    }

    public void startDropAnimation(int[][] newBoard, Runnable onComplete) {
        fallingCandies.clear();
        for (int c = 0; c < COLS; c++) {
            int emptySpaces = 0;
            for (int r = ROWS - 1; r >= 0; r--) {
                if (board[r][c] == -1) emptySpaces++;
                else if (emptySpaces > 0) {
                    fallingCandies.add(new FallingCandy(board[r][c], c, r, r + emptySpaces));
                    board[r + emptySpaces][c] = board[r][c]; board[r][c] = -1;
                }
            }
            for (int i = 0; i < emptySpaces; i++) {
                int newType = newBoard[i][c];
                fallingCandies.add(new FallingCandy(newType, c, -emptySpaces + i, i));
                board[i][c] = newType;
            }
        }
        if (fallingCandies.isEmpty()) { this.board = newBoard; if (onComplete != null) onComplete.run(); return; }
        isDropping = true;
        dropTimer = new Timer(15, e -> {
            boolean allDone = true;
            for (FallingCandy fc : fallingCandies) {
                if (fc.currentY < fc.targetY) { fc.currentY += 8; if (fc.currentY > fc.targetY) fc.currentY = fc.targetY; allDone = false; }
            }
            if (allDone) {
                dropTimer.stop(); isDropping = false; fallingCandies.clear(); this.board = newBoard;
                if (onComplete != null) onComplete.run();
            }
            repaint();
        });
        dropTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int x = PADDING + (c * TILE_SIZE);
                int y = PADDING + (r * TILE_SIZE);
                
                // ĐÃ XÓA LỆNH VẼ NỀN ĐEN (g2d.fillRoundRect) Ở ĐÂY
                g2d.setColor(new Color(0, 0, 0, 80)); 
                g2d.fillRoundRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4, 12, 12);
                
                if (board[r][c] == -1) continue;
                if (isAnimating && ((r == r1 && c == c1) || (r == r2 && c == c2))) continue; 
                
                boolean isFallingTarget = false;
                if (isDropping) {
                    for (FallingCandy fc : fallingCandies) {
                        if (fc.col == c && fc.endRow == r) { isFallingTarget = true; break; }
                    }
                }
                if (isFallingTarget) continue;

                boolean isThisCandyExploding = isExploding && explodingCandies.contains(new Point(r, c));
                drawCandy(g2d, board[r][c], x, y, isThisCandyExploding);

                // Viền trắng khi click chọn kẹo
                if (r == selectedRow && c == selectedCol) {
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawRoundRect(x, y, TILE_SIZE - 2, TILE_SIZE - 2, 12, 12);
                }
            }
        }

        if (isAnimating) {
            int baseX1 = PADDING + (c1 * TILE_SIZE); int baseY1 = PADDING + (r1 * TILE_SIZE);
            int baseX2 = PADDING + (c2 * TILE_SIZE); int baseY2 = PADDING + (r2 * TILE_SIZE);
            int currentX1 = baseX1 + (dx * animProgress); int currentY1 = baseY1 + (dy * animProgress);
            int currentX2 = baseX2 - (dx * animProgress); int currentY2 = baseY2 - (dy * animProgress);
            drawCandy(g2d, board[r1][c1], currentX1, currentY1, false);
            drawCandy(g2d, board[r2][c2], currentX2, currentY2, false);
        }

        if (isDropping) {
            Shape oldClip = g2d.getClip();
            g2d.clipRect(PADDING, PADDING, COLS * TILE_SIZE, ROWS * TILE_SIZE);
            for (FallingCandy fc : fallingCandies) {
                drawCandy(g2d, fc.type, PADDING + (fc.col * TILE_SIZE), (int) fc.currentY, false);
            }
            g2d.setClip(oldClip);
        }
    }

    private void drawCandy(Graphics2D g2d, int type, int x, int y, boolean isExplodingCandy) {
        if (type == -1) return; 
        if (isExplodingCandy && currentExplosionSize <= 0) return;
        int drawSize = TILE_SIZE - 10; 
        int drawX = x + 5; int drawY = y + 5;
        if (isExplodingCandy) {
            drawSize = currentExplosionSize;
            drawX = x + (TILE_SIZE - drawSize) / 2; drawY = y + (TILE_SIZE - drawSize) / 2;
        }
        if (candyImages != null && type >= 0 && type < candyImages.length && candyImages[type] != null) {
            g2d.drawImage(candyImages[type], drawX, drawY, drawSize, drawSize, null);
        } else {
            if(type >= 0 && type < colors.length) {
                g2d.setColor(colors[type]); g2d.fillOval(drawX, drawY, drawSize, drawSize);
            }
        }
    }
}